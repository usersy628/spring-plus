package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 클라이언트의 요청 값이 올바르지 않을 때 사용하는 예외입니다.
 */
public class InvalidRequestException extends DomainException {

	/**
	 * 잘못된 요청의 이유를 설명하는 메시지와 함께 400 응답 예외를 생성합니다.
	 *
	 * @param message 응답 본문에 담을 에러 메시지
	 */
	public InvalidRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
