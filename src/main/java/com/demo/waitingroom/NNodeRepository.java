package com.demo.waitingroom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NNodeRepository extends JpaRepository<NNode, Long>{
	Optional<NNode> findByWaitingRoomIdAndId(Long waitingRoomId, Long id);
	Optional<NNode> findTopByWaitingRoomIdOrderByPositionDesc(Long waitingRoomId);
	Optional<NNode> findTopByWaitingRoomIdOrderByPosition(Long waitingRoomId);
}
