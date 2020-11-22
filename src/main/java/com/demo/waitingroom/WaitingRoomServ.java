package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

public interface WaitingRoomServ {
	List<Patient> getAllPatients();

	Patient addPatient(Patient element);

	Optional<Patient> removePatient();

	void move(Patient patient, int position);

	int size();

	void clear();
}
