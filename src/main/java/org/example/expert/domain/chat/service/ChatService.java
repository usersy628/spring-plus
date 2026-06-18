package org.example.expert.domain.chat.service;

import java.util.List;

import org.example.expert.domain.chat.dto.request.ChatMessageRequest;
import org.example.expert.domain.chat.dto.response.ChatMessageResponse;
import org.example.expert.domain.chat.entity.ChatMessage;
import org.example.expert.domain.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessageResponse saveMessage(ChatMessageRequest request) {
		ChatMessage chatMessage = new ChatMessage(
			request.getRoomId(),
			request.getSender(),
			request.getMessage()
		);

		ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

		return new ChatMessageResponse(
			savedMessage.getRoomId(),
			savedMessage.getSender(),
			savedMessage.getMessage()
		);
	}

	public List<ChatMessageResponse> getMessages(String roomId) {
		return chatMessageRepository.findByRoomIdOrderBySentAtAsc(roomId)
			.stream()
			.map(chatMessage -> new ChatMessageResponse(
				chatMessage.getRoomId(),
				chatMessage.getSender(),
				chatMessage.getMessage()
			))
			.toList();
	}
}
