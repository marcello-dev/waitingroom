package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

public interface WaitingRoomServiceI {
	WaitingRoom create(String name);
	TextElement enqueue(Long id, TextElement el);
	Optional<TextElement> dequeue(Long id);
	List<TextElement> getAllElements(Long id);
	void move(Long id, Long elementId, int position);
	void delete(Long id);
}
