package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface NodeRepository<T> extends JpaRepository<Node<T>, Long> {

	List<Node<T>> findByOrderByPosition();
	
	Optional<Node<T>> findTopByOrderByPositionDesc();
	
	Optional<Node<T>> findTopByOrderByPosition();

	Optional<Node<T>> findByPosition(int position);
	
	Optional<Node<T>> findByValue(T value);
	
	List<Node<T>> findAllByPositionLessThan(int position, Pageable pages);
	
	List<Node<T>> findAllByPositionGreaterThan(int position, Pageable pages);
	
	List<Node<T>> findAllByPositionBetween(int start, int end);
}
