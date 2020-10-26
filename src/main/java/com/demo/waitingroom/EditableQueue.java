package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

public interface EditableQueue<T> {
	List<T> getAll();
	T enqueue(T element);
	Optional<T> dequeue();
	void move(T element, int position);
	int size();
	void clear();
}
