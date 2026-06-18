package org.example.expert.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String status;
	private final int code;
	private final String message;
	private final LocalDateTime timestamp;

	public ErrorResponse(HttpStatus status, String message) {
		this.status = status.name();
		this.code = status.value();
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}
