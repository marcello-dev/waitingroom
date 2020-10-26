package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface NodeRepository<T> extends JpaRepository<Node<T>, Long> {

	List<Node<T>> findByOrderByPosition();

	Optional<Node<T>> findByPosition(int position);
	
	Optional<Node<T>> findByValue(T value);

	Optional<Node<T>> findByFirstTrue();
	
	Optional<Node<T>> findByLastTrue();
	
	List<Node<T>> findAllByPositionBetween(int start, int end);
}
