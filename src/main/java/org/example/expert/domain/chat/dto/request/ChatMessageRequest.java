package org.example.expert.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

	private String roomId;
	private String sender;
	private String message;
}