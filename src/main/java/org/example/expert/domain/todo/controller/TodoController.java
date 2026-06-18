package org.example.expert.domain.todo.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 일정 생성, 목록 조회, 검색, 단건 조회 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;

	/**
	 * 인증된 사용자의 새 일정을 생성합니다.
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param todoSaveRequest 일정 생성 요청
	 * @return 생성된 일정 응답
	 */
	@PostMapping("/todos")
	public ResponseEntity<TodoSaveResponse> saveTodo(
		@AuthenticationPrincipal AuthUser authUser,
		@Valid @RequestBody TodoSaveRequest todoSaveRequest
	) {
		return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
	}

	/**
	 * 날씨와 수정일 기간 조건을 선택적으로 적용하여 일정 목록을 조회합니다.
	 *
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @param weather 날씨 조건
	 * @param createdAt 수정일 시작 조건
	 * @param modifiedAt 수정일 종료 조건
	 * @return 일정 목록 페이지
	 */
	@GetMapping("/todos")
	public ResponseEntity<Page<TodoResponse>> getTodos(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String weather,
		@RequestParam(required = false) LocalDateTime createdAt,
		@RequestParam(required = false) LocalDateTime modifiedAt

	) {
		return ResponseEntity.ok(todoService.getTodos(page, size, weather, createdAt, modifiedAt));
	}

	/**
	 * QueryDSL을 사용해 제목, 생성일 기간, 담당자 닉네임 조건으로 일정을 검색합니다.
	 *
	 * @param title 제목 검색어
	 * @param createdFrom 생성일 시작 조건
	 * @param createdTo 생성일 종료 조건
	 * @param managerNickname 담당자 닉네임 검색어
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 일정 검색 결과 페이지
	 */
	@GetMapping("/todos/search")
	public ResponseEntity<Page<TodoSearchResponse>> searchTodos(
		@RequestParam(required = false) String title,
		@RequestParam(required = false) LocalDateTime createdFrom,
		@RequestParam(required = false) LocalDateTime createdTo,
		@RequestParam(required = false) String managerNickname,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(
			todoService.searchTodos(title, createdFrom, createdTo, managerNickname, page, size));
	}

	/**
	 * 일정 ID로 단건 일정을 조회합니다.
	 *
	 * @param todoId 조회할 일정 ID
	 * @return 일정 단건 응답
	 */
	@GetMapping("/todos/{todoId}")
	public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
		return ResponseEntity.ok(todoService.getTodo(todoId));
	}
}
