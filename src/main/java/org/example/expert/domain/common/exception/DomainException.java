package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 도메인에서 발생하는 예외의 공통 부모 클래스입니다.
 *
 * <p>각 도메인 예외가 HTTP 상태값을 직접 가지도록 하여,
 * 전역 예외 처리기에서 일관된 에러 응답을 만들 수 있게 합니다.</p>
 */
@Getter
public class DomainException extends RuntimeException {

	private final HttpStatus status;

	/**
	 * 클라이언트에게 반환할 HTTP 상태값과 에러 메시지를 가진 도메인 예외를 생성합니다.
	 *
	 * @param status 에러 응답에 사용할 HTTP 상태값
	 * @param message 응답 본문에 담을 에러 메시지
	 */
	public DomainException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
