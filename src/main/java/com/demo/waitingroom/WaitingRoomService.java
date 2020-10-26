package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class WaitingRoomService implements WaitingRoom {

	private EditableQueue<Patient> queue;

	public WaitingRoomService(PersistentQueue<Patient> queue) {
		this.queue = queue;
	}

	@Override
	public List<Patient> getAllPatients() {
		return queue.getAll();
	}

	@Override
	public Patient addPatient(Patient patient) {
		return queue.enqueue(patient);
	}

	@Override
	public Optional<Patient> removePatient() {
		return queue.dequeue();
	}

	@Override
	public void move(Patient patient, int delta) {
		queue.move(patient, delta);
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public void clear() {
		queue.clear();
	}

}
