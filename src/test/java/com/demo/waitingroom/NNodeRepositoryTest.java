package com.demo.waitingroom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NNodeRepositoryTest {

    @Autowired
    NNodeRepository nnodeRepo;

    @Autowired
    WaitingRoomRepository wrRepo;

    @Test
    public void init(){
        WaitingRoom wr = new WaitingRoom("A");
        wr = wrRepo.save(wr);

        NNode node = new NNode(new TextElement("myMessage"));
        node.setPosition(1);
        wr.addNode(node);
        node.setWaitingRoom(wr);
        node = nnodeRepo.save(node);

        WaitingRoom actual = wrRepo.getOne(wr.getId());
        assertNotNull(actual);
        assertEquals(1, actual.getNodes().size());
        assertEquals("myMessage",actual.getNodes().get(0).getValue().getData());


        Optional<NNode> actualNode = nnodeRepo.findByWaitingRoomIdAndId(wr.getId(),node.getId());
        assertTrue(actualNode.isPresent());
        assertEquals("myMessage",actualNode.get().getValue().getData());

        actualNode = nnodeRepo.findTopByWaitingRoomIdOrderByPosition(wr.getId());
        assertTrue(actualNode.isPresent());
        assertEquals("myMessage",actualNode.get().getValue().getData());
    }
}
