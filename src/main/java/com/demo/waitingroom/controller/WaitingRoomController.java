package com.demo.waitingroom.controller;

import java.util.List;

import com.demo.waitingroom.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("waiting-room")
public class WaitingRoomController {

	static final Logger logger = LoggerFactory.getLogger(WaitingRoomController.class);
	@Autowired
	private EditableQueue<TextElement> queue;

	@Autowired
	private WaitingRoomServiceI waitingRoomService;
	
	@Autowired
	private WaitingRoomRepository wrRepo;
	@Autowired
	private NNodeRepository nnRepo;
	
	@PostMapping
	public WaitingRoom create(@RequestBody CreateWaitingRoom cwr) {
		logger.info("Creating waiting room name: {}",cwr.getName());
		return waitingRoomService.create(cwr.getName());
	}
	
	@GetMapping
	public List<WaitingRoom> getAllWaitingRooms(){
		logger.info("Get all waiting rooms");
		return wrRepo.findAll();
	}

	@DeleteMapping("/{id}")
	public void deleteWaitingRoom(@PathVariable Long id){
		logger.info("Delete waiting room {}",id);
		waitingRoomService.delete(id);
	}
	
	@PostMapping(value = "/{id}/enqueue")
	public TextElement enqueue(@PathVariable Long id, @RequestBody JsonNode requestBody) {
		logger.info("received arbitrary node: {}", requestBody);
		TextElement e = new TextElement(requestBody.toString());
		return waitingRoomService.enqueue(id,e);
	}

	@PostMapping(value = "/{id}/dequeue")
	public TextElement dequeue(@PathVariable Long id) {
		logger.info("received dequeue");
		return waitingRoomService.dequeue(id).orElse(new TextElement("not found"));
	}
}
