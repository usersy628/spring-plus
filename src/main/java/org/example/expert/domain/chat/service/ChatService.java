package org.example.expert.domain.chat.service;

import java.util.List;

import org.example.expert.domain.chat.dto.request.ChatMessageRequest;
import org.example.expert.domain.chat.dto.response.ChatMessageResponse;
import org.example.expert.domain.chat.entity.ChatMessage;
import org.example.expert.domain.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 채팅 메시지 저장과 조회 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;

	/**
	 * 전달받은 채팅 메시지를 DB에 저장하고 브로드캐스트용 응답 DTO로 변환합니다.
	 *
	 * @param request 저장할 채팅 메시지 요청
	 * @return 저장된 메시지 정보를 담은 응답
	 */
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

	/**
	 * 특정 채팅방의 메시지를 보낸 시간 오름차순으로 조회합니다.
	 *
	 * @param roomId 조회할 채팅방 ID
	 * @return 채팅 메시지 응답 목록
	 */
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
