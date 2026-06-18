package org.example.expert.domain.auth.exception;

import org.example.expert.domain.common.exception.DomainException;
import org.springframework.http.HttpStatus;

public class AuthException extends DomainException {

	public AuthException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
