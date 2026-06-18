package org.example.expert.global.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 도메인 예외가 기대한 공통 에러 응답으로 변환되는지 검증하는 테스트입니다.
 */
class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void domainException_상태값으로_에러응답을_생성한다() {
		ResponseEntity<ErrorResponse> response = handler.handleDomainException(
			new InvalidRequestException("잘못된 요청입니다.")
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.name());
		assertThat(response.getBody().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
	}

	@Test
	void authException은_인증실패_응답을_생성한다() {
		ResponseEntity<ErrorResponse> response = handler.handleDomainException(
			new AuthException("인증에 실패했습니다.")
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.name());
		assertThat(response.getBody().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		assertThat(response.getBody().getMessage()).isEqualTo("인증에 실패했습니다.");
	}

	@Test
	void serverException은_서버에러_응답을_생성한다() {
		ResponseEntity<ErrorResponse> response = handler.handleDomainException(
			new ServerException("서버 오류입니다.")
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.name());
		assertThat(response.getBody().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertThat(response.getBody().getMessage()).isEqualTo("서버 오류입니다.");
	}
}
