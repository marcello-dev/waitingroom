package com.demo.waitingroom;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class WaitingRoom {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@OneToMany(mappedBy="waitingRoom")
	private List<NNode> nodes;
	
	public WaitingRoom(String name) {
		this.name=name;
	}
}
