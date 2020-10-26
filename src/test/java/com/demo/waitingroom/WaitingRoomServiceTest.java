package com.demo.waitingroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WaitingRoomServiceTest {

	@Autowired
	private WaitingRoom waitingRoom;

	@BeforeEach
	public void clearRoom() {
		waitingRoom.clear();
	}

	@Test
	public void enqueueDequeueTest() {
		waitingRoom.addPatient(new Patient("Marc"));
		waitingRoom.addPatient(new Patient("Anna"));

		assertTrue(waitingRoom.size() == 2);
		assertEquals("Marc", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 1);
		assertEquals("Anna", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 0);
	}

	@Test
	public void moveTest() {
		waitingRoom.addPatient(new Patient("p1"));
		waitingRoom.addPatient(new Patient("p2"));
		Patient p3 = waitingRoom.addPatient(new Patient("p3"));
		waitingRoom.addPatient(new Patient("p4"));

		// Move p3 up by 1 position
		waitingRoom.move(p3, 1);

		assertEquals("p1", waitingRoom.removePatient().get().getName());
		assertEquals("p3", waitingRoom.removePatient().get().getName());
		assertEquals("p2", waitingRoom.removePatient().get().getName());
		assertEquals("p4", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 0);
	}
	
	@Test
	public void moveTest2() {
		waitingRoom.addPatient(new Patient("p1"));
		waitingRoom.addPatient(new Patient("p2"));
		Patient p3 = waitingRoom.addPatient(new Patient("p3"));
		Patient p4 = waitingRoom.addPatient(new Patient("p4"));
		waitingRoom.addPatient(new Patient("p5"));

		// Move p3 up by 2 position
		waitingRoom.move(p3, 2);
		// Move p4 down by 2 position
		waitingRoom.move(p4, -1);
		
		assertEquals("p3", waitingRoom.removePatient().get().getName());
		assertEquals("p1", waitingRoom.removePatient().get().getName());
		assertEquals("p2", waitingRoom.removePatient().get().getName());
		assertEquals("p5", waitingRoom.removePatient().get().getName());
		assertEquals("p4", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 0);
	}
	
	@Test
	public void moveTest3() {
		waitingRoom.addPatient(new Patient("p1"));
		Patient p2 = waitingRoom.addPatient(new Patient("p2"));

		waitingRoom.move(p2, 1);
		waitingRoom.move(p2, -1);
		
		assertEquals("p1", waitingRoom.removePatient().get().getName());
		assertEquals("p2", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 0);
	}
	
	@Test
	public void moveTest4() {
		waitingRoom.addPatient(new Patient("p1"));
		Patient p2 = waitingRoom.addPatient(new Patient("p2"));
		waitingRoom.addPatient(new Patient("p3"));
		waitingRoom.addPatient(new Patient("p4"));

		waitingRoom.removePatient();
		waitingRoom.move(p2, -1);
		
		assertEquals("p3", waitingRoom.removePatient().get().getName());
		assertEquals("p2", waitingRoom.removePatient().get().getName());
		assertEquals("p4", waitingRoom.removePatient().get().getName());
		assertTrue(waitingRoom.size() == 0);
	}

	@Test
	public void stressTest() {
		int n = 1000;
		
		long startTime = System.nanoTime();
		for (long i = 1; i < n; i++) {
			waitingRoom.addPatient(new Patient("p" + i));
		}
		long endTime = System.nanoTime();
		System.out.format("Enqueue %d time: %d ms%n", n, (endTime - startTime) / 1000000);
		
		startTime = System.nanoTime();
		for (long i = 1; i < n; i++) {
			waitingRoom.removePatient();
		}
		endTime = System.nanoTime();
		System.out.format("Dequeue %d time: %d ms%n", n, (endTime - startTime) / 1000000);

		assertTrue(waitingRoom.size() == 0);
	}

	@Test
	public void getAllPatientsTest() {
		waitingRoom.addPatient(new Patient("p1"));
		waitingRoom.addPatient(new Patient("p2"));
		waitingRoom.addPatient(new Patient("p3"));

		List<Patient> patients = waitingRoom.getAllPatients();

		assertEquals("p1", patients.get(0).getName());
		assertEquals("p2", patients.get(1).getName());
		assertEquals("p3", patients.get(2).getName());
	}
}
