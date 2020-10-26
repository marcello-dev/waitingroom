package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface WaitingPatientRepository extends JpaRepository<WaitingPatient, Long> {

	List<WaitingPatient> findByOrderByPosition();

	Optional<WaitingPatient> findByPosition(int position);
	
	Optional<WaitingPatient> findByPatient(Patient patient);

	Optional<WaitingPatient> findTopByOrderByPositionDesc();
	
	Optional<WaitingPatient> findByFirstTrue();
	
	Optional<WaitingPatient> findByLastTrue();
	
	Optional<WaitingPatient> findTopByOrderByPosition();

	List<WaitingPatient> findAllByPositionBetween(int start, int end);
}
