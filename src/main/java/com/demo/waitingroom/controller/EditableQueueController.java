package com.demo.waitingroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class EditableQueueController {
	
	static final Logger logger = LoggerFactory.getLogger(EditableQueueController.class);
	
	@GetMapping
	public String get() {
		return "hello";
	}
	
	@PostMapping(value="/push", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_PLAIN_VALUE)
	public String push(@RequestBody JsonNode requestBody) {
		logger.info("received arbitrary node: {}",requestBody);
		return "{\"id\":1}";
	}
}
