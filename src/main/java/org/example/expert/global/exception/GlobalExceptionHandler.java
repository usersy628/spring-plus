package org.example.expert.global.exception;

import org.example.expert.domain.common.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 컨트롤러에서 발생한 예외를 공통 HTTP 에러 응답으로 변환하는 전역 예외 처리기입니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * HTTP 상태값을 가지고 있는 도메인 예외를 처리합니다.
	 *
	 * @param ex 애플리케이션에서 발생한 도메인 예외
	 * @return 공통 에러 응답 본문을 담은 응답 엔티티
	 */
	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
		HttpStatus status = ex.getStatus();
		if (status.is5xxServerError()) {
			log.error("Domain server exception", ex);
		}
		return getErrorResponse(status, ex.getMessage());
	}

	/**
	 * 요청 DTO 검증 실패 예외를 처리합니다.
	 *
	 * @param ex 스프링 검증 과정에서 발생한 예외
	 * @return 첫 번째 검증 실패 메시지를 담은 응답 엔티티
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("Invalid request");

        return getErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

	/**
	 * 별도로 처리되지 않은 예상 밖의 예외를 처리합니다.
	 *
	 * @param ex 예상하지 못한 예외
	 * @return 공통 서버 에러 메시지를 담은 응답 엔티티
	 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

	/**
	 * 공통 에러 응답 엔티티를 생성합니다.
	 *
	 * @param status 응답에 사용할 HTTP 상태값
	 * @param message 응답 본문에 담을 에러 메시지
	 * @return 공통 에러 응답 본문을 담은 응답 엔티티
	 */
    public ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(status, message));
    }
}

