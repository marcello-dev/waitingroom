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
public class WaitingPatient {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Patient patient;
	private int position;
	// Head
	private boolean first;
	// Tail
	private boolean last;

	public WaitingPatient(Patient patient) {
		this.patient = patient;
		this.position = -1;
	}
}
