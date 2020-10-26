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

	private WaitingPatientRepository patientRepo;

	public PersistentWaitingRoom(WaitingPatientRepository patientRepo) {
		this.patientRepo = patientRepo;
	}

	@Override
	public List<Patient> getAll() {
		return patientRepo.findByOrderByPosition().stream().map(wp -> wp.getPatient()).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Patient enqueue(Patient patient) {
		WaitingPatient wp = new WaitingPatient(patient);
		Optional<WaitingPatient> lastPatient = patientRepo.findByLastTrue();
		int newPosition = 0;
		if (lastPatient.isPresent()) {
			newPosition = lastPatient.get().getPosition() + 1;
			lastPatient.get().setLast(false);
		} else {
			wp.setFirst(true);
		}
		wp.setPosition(newPosition);
		wp.setLast(true);
		return patientRepo.save(wp).getPatient();
	}

	@Override
	@Transactional
	public Optional<Patient> dequeue() {
		if (size() == 0) {
			return Optional.empty();
		}
		Optional<WaitingPatient> first = patientRepo.findByFirstTrue();
		Optional<WaitingPatient> second = patientRepo.findByPosition(first.get().getPosition() + 1);
		if (second.isPresent()) {
			second.get().setFirst(true);
		}
		patientRepo.deleteById(first.get().getId());
		return Optional.of(first.get().getPatient());
	}

	@Override
	@Transactional
	public void move(Patient patient, int delta) {
		WaitingPatient toMove = patientRepo.findByPatient(patient).get();
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
		for (WaitingPatient p : patientRepo.findAllByPositionBetween(start, end)) {
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
		return (int) patientRepo.count();
	}

	@Override
	public void clear() {
		patientRepo.deleteAll();
	}

}
