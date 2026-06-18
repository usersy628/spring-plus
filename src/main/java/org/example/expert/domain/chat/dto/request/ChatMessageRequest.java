package org.example.expert.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트가 STOMP로 전송하는 채팅 메시지 요청 DTO입니다.
 */
@Getter
@NoArgsConstructor
public class ChatMessageRequest {

	private String roomId;
	private String sender;
	private String message;
}
