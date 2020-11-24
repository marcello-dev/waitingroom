package com.demo.waitingroom;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WaitingRoomServiceImpl implements WaitingRoomServiceI{
	private final static int POSITION_GAP = 100;
	private final WaitingRoomRepository wrRepo;
	private final NNodeRepository nodeRepo;

	public WaitingRoomServiceImpl(WaitingRoomRepository wrRepo, NNodeRepository nodeRepo){
		this.wrRepo=wrRepo;
		this.nodeRepo=nodeRepo;
	}
	
	@Override
	public WaitingRoom create(String name) {
		return wrRepo.save(new WaitingRoom(name));
	}

	@Override
	public TextElement enqueue(Long id, TextElement el) {
		Objects.requireNonNull(el);
		NNode node = new NNode(el);
		WaitingRoom wr = wrRepo.findById(id).get();
		Optional<NNode> lastNode = nodeRepo.findTopByWaitingRoomIdOrderByPositionDesc(id);
		int newPosition = lastNode.map(n -> n.getPosition() + POSITION_GAP).orElse(POSITION_GAP);
		node.setPosition(newPosition);
		node.setWaitingRoom(wr);
		return nodeRepo.save(node).getValue();
	}

	@Override
	public Optional<TextElement> dequeue(Long id) {
		Optional<NNode> firstNode = nodeRepo.findTopByWaitingRoomIdOrderByPosition(id);
		if(firstNode.isPresent()) {
			nodeRepo.deleteById(firstNode.get().getId());
			return Optional.of(firstNode.get().getValue());
		}
		return Optional.empty();
	}

	@Override
	public List<TextElement> getAllElements(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(Long id, Long elementId, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		wrRepo.deleteById(id);
	}

}
