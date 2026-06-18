package org.example.expert.domain.chat.repository;

import java.util.List;

import org.example.expert.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByRoomIdOrderBySentAtAsc(String roomId);
}