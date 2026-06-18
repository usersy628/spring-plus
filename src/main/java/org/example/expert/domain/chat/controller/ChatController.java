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

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService;

	@MessageMapping("/chat/message")
	public void sendMessage(ChatMessageRequest request) {
		ChatMessageResponse response = chatService.saveMessage(request);

		messagingTemplate.convertAndSend(
			"/sub/chat/rooms/" + request.getRoomId(),
			response
		);
	}

	@GetMapping("/chat/rooms/{roomId}/messages")
	public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable String roomId) {
		return ResponseEntity.ok(chatService.getMessages(roomId));
	}
}