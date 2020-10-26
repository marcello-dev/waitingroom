package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class PersistentQueue<T> implements EditableQueue<T> {

	private NodeRepository<T> nodeRepo;

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
	public T enqueue(T element) {
		Node<T> node = new Node<>(element);
		Optional<Node<T>> lastNode = nodeRepo.findByLastTrue();
		int newPosition = 0;
		if (lastNode.isPresent()) {
			newPosition = lastNode.get().getPosition() + 1;
			lastNode.get().setLast(false);
		} else {
			node.setFirst(true);
		}
		node.setPosition(newPosition);
		node.setLast(true);
		return nodeRepo.save(node).getValue();
	}

	@Override
	@Transactional
	public Optional<T> dequeue() {
		if (size() == 0) {
			return Optional.empty();
		}
		Optional<Node<T>> first = nodeRepo.findByFirstTrue();
		Optional<Node<T>> second = nodeRepo.findByPosition(first.get().getPosition() + 1);
		if (second.isPresent()) {
			second.get().setFirst(true);
		}
		nodeRepo.deleteById(first.get().getId());
		return Optional.of(first.get().getValue());
	}

	@Override
	@Transactional
	public void move(T element, int delta) {
		Node<T> toMove = nodeRepo.findByValue(element).get();
		int newPosition = toMove.getPosition() - delta;
		int next = delta > 0 ? toMove.getPosition() - 1 : toMove.getPosition() + 1;
		int start, end = 0;
		if (delta > 0) {
			start = newPosition;
			end = next;
		} else {
			start = next;
			end = newPosition;
		}
		for (Node<T> p : nodeRepo.findAllByPositionBetween(start, end)) {
			if (delta > 0) {
				// swap head with node to move
				if (p.isFirst()) {
					p.setFirst(false);
					toMove.setFirst(true);
				}
				// swap tail with node to move
				if (toMove.isLast() && p.getPosition() == next) {
					toMove.setLast(false);
					p.setLast(true);
				}
			} else {
				if (p.isLast()) {
					p.setLast(false);
					toMove.setLast(true);
				}
				if (toMove.isFirst() && p.getPosition() == next) {
					toMove.setFirst(false);
					p.setFirst(true);
				}
			}
			p.setPosition(delta > 0 ? p.getPosition() + 1 : p.getPosition() - 1);
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
