package org.example.expert.domain.log.entity;

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
@Table(name = "logs")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long requesterId;
	private Long todoId;
	private Long managerUserId;
	private String requestType;
	private LocalDateTime createdAt;

	public Log(Long requesterId, Long todoId, Long managerUserId, String requestType) {
		this.requesterId = requesterId;
		this.todoId = todoId;
		this.managerUserId = managerUserId;
		this.requestType = requestType;
		this.createdAt = LocalDateTime.now();
	}
}
