package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 서버 내부에서 예상하지 못한 문제가 발생했을 때 사용하는 예외입니다.
 */
public class ServerException extends DomainException {

	/**
	 * 서버 오류 내용을 설명하는 메시지와 함께 500 응답 예외를 생성합니다.
	 *
	 * @param message 응답 본문에 담을 에러 메시지
	 */
	public ServerException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
