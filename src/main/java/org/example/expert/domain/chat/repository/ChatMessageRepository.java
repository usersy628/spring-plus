package org.example.expert.domain.chat.repository;

import java.util.List;

import org.example.expert.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 채팅 메시지 저장과 채팅방별 메시지 조회를 담당하는 Repository입니다.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	/**
	 * 특정 채팅방의 메시지를 발송 시각 오름차순으로 조회합니다.
	 *
	 * @param roomId 조회할 채팅방 ID
	 * @return 시간순으로 정렬된 채팅 메시지 목록
	 */
	List<ChatMessage> findByRoomIdOrderBySentAtAsc(String roomId);
}
