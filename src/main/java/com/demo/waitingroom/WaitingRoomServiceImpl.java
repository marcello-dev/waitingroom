package com.demo.waitingroom;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
		Optional<NNode> lastNodeOp = nodeRepo.findTopByWaitingRoomIdOrderByPositionDesc(id);
		int newPosition = 0;
		WaitingRoom wr = null;
		if(lastNodeOp.isPresent()) {
			NNode lastNode = lastNodeOp.get();
			newPosition = lastNode.getPosition() + POSITION_GAP;
			wr = lastNode.getWaitingRoom();
		} else {
			newPosition = POSITION_GAP;
			wr = wrRepo.findById(id).get();
		}
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
		return nodeRepo.findAllByWaitingRoomId(id)
				.stream().map(n->n.getValue())
				.collect(Collectors.toList());
	}

	@Override
	public void move(Long id, Long elementId, int delta) {
		Optional<NNode> optionalNode = nodeRepo.findByValueId(elementId);
		if(!optionalNode.isPresent()) {
			throw new NoSuchElementException("Value not found in the queue");
		}
		NNode toMove = optionalNode.get();
		int newPosition = 0;
		if(delta>0) {
			List<NNode> nodes = nodeRepo.findAllByWaitingRoomIdAndPositionLessThan(id, toMove.getPosition(), PageRequest.of(0, delta+1,Sort.by("position").ascending()));
			if(nodes.size()<delta+1) {
				newPosition = nodes.get(0).getPosition() / 2;
			} else {
				newPosition = (nodes.get(0).getPosition() + nodes.get(1).getPosition()) / 2;
				// TODO handle re-balancing
			}
		} else {
			delta = Math.abs(delta);
			List<NNode> nodes = nodeRepo.findAllByWaitingRoomIdAndPositionGreaterThan(id, toMove.getPosition(), PageRequest.of(0, delta+1,Sort.by("position").descending()));
			if(nodes.size()<delta+1) {
				newPosition = nodes.get(0).getPosition() + POSITION_GAP;
			} else {
				newPosition = (nodes.get(0).getPosition() + nodes.get(1).getPosition()) / 2;
				// TODO handle re-balancing
			}
		}
		toMove.setPosition(newPosition);
	}

	@Override
	public void delete(Long id) {
		wrRepo.deleteById(id);
	}

}
