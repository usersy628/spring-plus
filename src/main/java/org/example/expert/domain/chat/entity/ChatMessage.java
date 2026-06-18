package org.example.expert.domain.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅방에 저장되는 개별 채팅 메시지 엔티티입니다.
 */
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

	/**
	 * 채팅 메시지를 생성하며 발송 시각은 현재 시간으로 기록합니다.
	 *
	 * @param roomId 메시지가 속한 채팅방 ID
	 * @param sender 메시지를 보낸 사용자 이름
	 * @param message 메시지 내용
	 */
	public ChatMessage(String roomId, String sender, String message) {
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.sentAt = LocalDateTime.now();
	}
}
