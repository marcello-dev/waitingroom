package com.demo.waitingroom;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
	private boolean first;
	private boolean last;

	public Node(T value) {
		this.value = value;
		this.position = -1;
	}
}
