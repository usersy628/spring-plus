package org.example.expert.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {

	private final String roomId;
	private final String sender;
	private final String message;
}