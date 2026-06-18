package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL로 직접 구현하는 일정 조회 기능을 정의하는 커스텀 Repository입니다.
 */
public interface TodoRepositoryCustom {

	/**
	 * 일정과 작성자를 함께 조회합니다.
	 *
	 * @param todoId 조회할 일정 ID
	 * @return 작성자가 함께 로딩된 일정
	 */
	Optional<Todo> findByIdWithUser(Long todoId);

	/**
	 * 일정 검색 조건과 페이징 정보를 이용해 검색 결과 DTO를 조회합니다.
	 *
	 * @param title 제목 검색어
	 * @param createdFrom 생성일 시작 조건
	 * @param createdTo 생성일 종료 조건
	 * @param managerNickname 담당자 닉네임 검색어
	 * @param pageable 페이지 요청 정보
	 * @return 일정 검색 결과 페이지
	 */
	Page<TodoSearchResponse> searchTodosWithQueryDsl(
		String title,
		LocalDateTime createdFrom,
		LocalDateTime createdTo,
		String managerNickname,
		Pageable pageable
	);
}
