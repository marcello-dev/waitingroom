package com.demo.waitingroom;

import java.util.List;

public interface WaitingRoomServiceI {
	WaitingRoom create(String name);
	Element enqueue(Long id, Element el);
	Element dequeue(Long id);
	List<Element> getAllElements(Long id);
	void move(Long id, Long elementId, int position);
	void delete(Long id);
}
