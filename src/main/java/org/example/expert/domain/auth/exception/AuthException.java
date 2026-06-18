package org.example.expert.domain.auth.exception;

import org.example.expert.domain.common.exception.DomainException;
import org.springframework.http.HttpStatus;

/**
 * 인증에 실패했을 때 사용하는 예외입니다.
 */
public class AuthException extends DomainException {

	/**
	 * 인증 실패 이유를 설명하는 메시지와 함께 401 응답 예외를 생성합니다.
	 *
	 * @param message 응답 본문에 담을 에러 메시지
	 */
	public AuthException(String message) {
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
