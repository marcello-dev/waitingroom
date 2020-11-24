package com.demo.waitingroom;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PersistentQueue<T> implements EditableQueue<T> {

	private NodeRepository<T> nodeRepo;
	private final static int POSITION_GAP = 100;

	public PersistentQueue(NodeRepository<T> patientRepo) {
		this.nodeRepo = patientRepo;
	}

	@Override
	public List<T> getAll() {
		return nodeRepo.findByOrderByPosition()
				.stream()
				.map(node -> node.getValue())
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public T enqueue(T value) {
		Objects.requireNonNull(value);
		Node<T> node = new Node<>(value);
		Optional<Node<T>> lastNode = nodeRepo.findTopByOrderByPositionDesc();
		int newPosition = lastNode.isPresent() ? lastNode.get().getPosition() + POSITION_GAP : POSITION_GAP;
		node.setPosition(newPosition);
		return nodeRepo.save(node).getValue();
	}

	@Override
	@Transactional
	public Optional<T> dequeue() {
		Optional<Node<T>> firstNode = nodeRepo.findTopByOrderByPosition();
		if(firstNode.isPresent()) {
			nodeRepo.deleteById(firstNode.get().getId());
			return Optional.of(firstNode.get().getValue());
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public void move(T value, int delta) {
		Objects.requireNonNull(value);
		Optional<Node<T>> optionalNode = nodeRepo.findByValue(value);
		if(!optionalNode.isPresent()) {
			throw new NoSuchElementException("Value not found in the queue");
		}
		Node<T> toMove = optionalNode.get();
		int newPosition = 0;
		if(delta>0) {
			List<Node<T>> nodes = nodeRepo.findAllByPositionLessThan(toMove.getPosition(), PageRequest.of(0, delta+1,Sort.by("position").ascending()));
			if(nodes.size()<delta+1) {
				newPosition = nodes.get(0).getPosition() / 2;
			} else {
				newPosition = (nodes.get(0).getPosition() + nodes.get(1).getPosition()) / 2;
				// TODO handle re-balancing
			}
		} else {
			delta = Math.abs(delta);
			List<Node<T>> nodes = nodeRepo.findAllByPositionGreaterThan(toMove.getPosition(), PageRequest.of(0, delta+1,Sort.by("position").descending()));
			if(nodes.size()<delta+1) {
				newPosition = nodes.get(0).getPosition() + POSITION_GAP;
			} else {
				newPosition = (nodes.get(0).getPosition() + nodes.get(1).getPosition()) / 2;
				// TODO handle re-balancing
			}
		}
		toMove.setPosition(newPosition);
	}

	@Override
	public int size() {
		return (int) nodeRepo.count();
	}

	@Override
	public void clear() {
		nodeRepo.deleteAll();
	}

}
