package com.demo.waitingroom;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WaitingRoomServiceImpl implements WaitingRoomServiceI{

	private WaitingRoomRepository wrRepo;
	private NNodeRepository nodeRepo;
	
	@Override
	public WaitingRoom create(String name) {
		return wrRepo.save(new WaitingRoom(name));
	}

	@Override
	public Element enqueue(Long id, Element el) {
		Objects.requireNonNull(el);
		NNode node = new NNode(el);
//		Optional<Node> lastNode = nodeRepo.findTopByOrderByPositionDesc();
//		int newPosition = lastNode.isPresent() ? lastNode.get().getPosition() + POSITION_GAP : POSITION_GAP;
//		node.setPosition(newPosition);
//		return nodeRepo.save(node).getValue();
		
		return null;
	}

	@Override
	public Element dequeue(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Element> getAllElements(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(Long id, Long elementId, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
