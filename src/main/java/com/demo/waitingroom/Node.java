package com.demo.waitingroom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Node<T> {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne(cascade = CascadeType.PERSIST, targetEntity = Patient.class)
	private T value;
	private int position;

	public Node(T value) {
		this.value = value;
		this.position = -1;
	}
}
