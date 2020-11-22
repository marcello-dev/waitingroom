package com.demo.waitingroom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.waitingroom.EditableQueue;
import com.demo.waitingroom.Element;
import com.demo.waitingroom.NNodeRepository;
import com.demo.waitingroom.WaitingRoomRepository;
import com.demo.waitingroom.PersistentQueue;
import com.demo.waitingroom.WaitingRoom;
import com.fasterxml.jackson.databind.JsonNode;

@RestController(value="/waiting-room")
public class WaitingRoomController {

	static final Logger logger = LoggerFactory.getLogger(WaitingRoomController.class);
	@Autowired
	private EditableQueue<Element> queue;
	
	@Autowired
	private WaitingRoomRepository wrRepo;
	@Autowired
	private NNodeRepository nnRepo;
	
	@PostMapping
	public WaitingRoom createWaitingRoom(WaitingRoom room) {
		logger.info("Creating waiting room");
		return wrRepo.save(room);
	}
	
	@GetMapping
	public List<WaitingRoom> getAllWaitingRooms(){
		logger.info("Get all waiting rooms");
		return wrRepo.findAll();
	}

	@DeleteMapping("/{id}")
	public void deleteWaitingRoom(Long id){
		logger.info("Delete waiting room {}",id);
		wrRepo.deleteById(id);
	}
	
	@PostMapping(value = "/{id}/enqueue", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String push(Long id, @RequestBody JsonNode requestBody) {
		logger.info("received arbitrary node: {}", requestBody);
		
		logger.info("found: "+nnRepo.findByWaitingRoomIdAndId(id, 0L));
		
		Element e = new Element(requestBody.toString());
		e = queue.enqueue(e);
		return "{\"id\":" + e.getId() + "}";
	}
}
