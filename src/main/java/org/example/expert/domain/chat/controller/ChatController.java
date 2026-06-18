package org.example.expert.domain.chat.controller;

import java.util.List;

import org.example.expert.domain.chat.dto.request.ChatMessageRequest;
import org.example.expert.domain.chat.dto.response.ChatMessageResponse;
import org.example.expert.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

/**
 * STOMP 채팅 메시지 송수신과 채팅 내역 조회 요청을 처리하는 컨트롤러입니다.
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService;

	/**
	 * 클라이언트가 보낸 채팅 메시지를 저장한 뒤 해당 채팅방 구독자에게 전달합니다.
	 *
	 * @param request 채팅방 ID, 발신자, 메시지 내용을 담은 요청
	 */
	@MessageMapping("/chat/message")
	public void sendMessage(ChatMessageRequest request) {
		ChatMessageResponse response = chatService.saveMessage(request);

		messagingTemplate.convertAndSend(
			"/sub/chat/rooms/" + request.getRoomId(),
			response
		);
	}

	/**
	 * 특정 채팅방에 저장된 메시지 목록을 시간순으로 조회합니다.
	 *
	 * @param roomId 조회할 채팅방 ID
	 * @return 채팅 메시지 응답 목록
	 */
	@GetMapping("/chat/rooms/{roomId}/messages")
	public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable String roomId) {
		return ResponseEntity.ok(chatService.getMessages(roomId));
	}
}
