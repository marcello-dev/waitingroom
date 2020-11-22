package com.demo.waitingroom;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class NNode {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne(cascade = CascadeType.PERSIST)
	private Element value;
	@ManyToOne
    @JoinColumn(name="waiting_room_id", nullable=false)
    private WaitingRoom waitingRoom;
	private int position;

	public NNode(Element value) {
		this.value = value;
		this.position = -1;
	}
}
