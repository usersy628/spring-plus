package org.example.expert.domain.log.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 담당자 등록 요청 기록을 저장하는 로그 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "log")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long requesterId;
	private Long todoId;
	private Long managerUserId;
	private String requestType;
	private LocalDateTime createdAt;

	/**
	 * 담당자 등록 요청 로그를 생성합니다.
	 *
	 * @param requesterId 요청을 보낸 사용자 ID
	 * @param todoId 대상 일정 ID
	 * @param managerUserId 등록하려는 담당자 사용자 ID
	 * @param requestType 요청 종류
	 */
	public Log(Long requesterId, Long todoId, Long managerUserId, String requestType) {
		this.requesterId = requesterId;
		this.todoId = todoId;
		this.managerUserId = managerUserId;
		this.requestType = requestType;
		this.createdAt = LocalDateTime.now();
	}
}
