package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("persistent")
public class PersistentWaitingRoom implements WaitingRoom<Patient> {

	private NodeRepository nodeRepo;

	public PersistentWaitingRoom(NodeRepository patientRepo) {
		this.nodeRepo = patientRepo;
	}

	@Override
	public List<Patient> getAll() {
		return nodeRepo.findByOrderByPosition()
				.stream()
				.map(node -> node.getPatient())
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Patient enqueue(Patient patient) {
		Node node = new Node(patient);
		Optional<Node> lastNode = nodeRepo.findByLastTrue();
		int newPosition = 0;
		if (lastNode.isPresent()) {
			newPosition = lastNode.get().getPosition() + 1;
			lastNode.get().setLast(false);
		} else {
			node.setFirst(true);
		}
		node.setPosition(newPosition);
		node.setLast(true);
		return nodeRepo.save(node).getPatient();
	}

	@Override
	@Transactional
	public Optional<Patient> dequeue() {
		if (size() == 0) {
			return Optional.empty();
		}
		Optional<Node> first = nodeRepo.findByFirstTrue();
		Optional<Node> second = nodeRepo.findByPosition(first.get().getPosition() + 1);
		if (second.isPresent()) {
			second.get().setFirst(true);
		}
		nodeRepo.deleteById(first.get().getId());
		return Optional.of(first.get().getPatient());
	}

	@Override
	@Transactional
	public void move(Patient patient, int delta) {
		Node toMove = nodeRepo.findByPatient(patient).get();
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
		for (Node p : nodeRepo.findAllByPositionBetween(start, end)) {
			if (delta > 0) {
				// swap head with toMove
				if (p.isFirst()) {
					p.setFirst(false);
					toMove.setFirst(true);
				}
				// swap tail with next
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
