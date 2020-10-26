package com.demo.waitingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Patient {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public Patient(String name) {
		this.name = name;
	}
}
