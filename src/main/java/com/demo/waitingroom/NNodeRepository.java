package com.demo.waitingroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NNodeRepository extends JpaRepository<NNode, Long>{
	Optional<NNode> findByWaitingRoomIdAndId(Long waitingRoomId, Long id);
	Optional<NNode> findTopByWaitingRoomIdOrderByPositionDesc(Long waitingRoomId);
	Optional<NNode> findTopByWaitingRoomIdOrderByPosition(Long waitingRoomId);
	List<NNode> findAllByWaitingRoomIdAndPositionLessThan(Long waitingRoomId, int position, Pageable pages);
	List<NNode> findAllByWaitingRoomIdAndPositionGreaterThan(Long waitingRoomId, int position, Pageable pages);
	Optional<NNode> findByValueId(Long valueId);
	List<NNode> findAllByWaitingRoomId(Long waitingRoomId);
}
