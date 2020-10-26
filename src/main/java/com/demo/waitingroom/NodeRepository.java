package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface NodeRepository extends JpaRepository<Node, Long> {

	List<Node> findByOrderByPosition();

	Optional<Node> findByPosition(int position);
	
	Optional<Node> findByPatient(Patient patient);

	Optional<Node> findByFirstTrue();
	
	Optional<Node> findByLastTrue();
	
	List<Node> findAllByPositionBetween(int start, int end);
}
