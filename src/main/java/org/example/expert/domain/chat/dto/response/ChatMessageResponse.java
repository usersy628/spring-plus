package org.example.expert.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 저장 또는 조회된 채팅 메시지를 클라이언트에게 전달하는 응답 DTO입니다.
 */
@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {

	private final String roomId;
	private final String sender;
	private final String message;
}
