package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

	private final HttpStatus status;

	public DomainException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
