package org.example.expert.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 전역 예외 처리기에서 반환하는 공통 에러 응답 본문입니다.
 */
@Getter
public class ErrorResponse {

	private final String status;
	private final int code;
	private final String message;
	private final LocalDateTime timestamp;

	/**
	 * HTTP 상태값과 에러 메시지를 기반으로 일관된 에러 응답을 생성합니다.
	 *
	 * @param status 응답에 사용할 HTTP 상태값
	 * @param message 클라이언트에게 전달할 에러 메시지
	 */
	public ErrorResponse(HttpStatus status, String message) {
		this.status = status.name();
		this.code = status.value();
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}
