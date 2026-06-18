package org.example.expert.domain.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomId;
	private String sender;
	private String message;
	private LocalDateTime sentAt;

	public ChatMessage(String roomId, String sender, String message) {
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.sentAt = LocalDateTime.now();
	}
}