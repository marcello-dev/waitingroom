package com.demo.waitingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Element {
	@Id
	@GeneratedValue
	private Long id;
	private String data;
	
	public Element(String data) {
		this.data = data;
	}
}
