package org.example.expert.domain.todo.service;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 일정 생성, 목록 조회, 검색, 단건 조회 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

	private final TodoRepository todoRepository;
	private final WeatherClient weatherClient;

	/**
	 * 오늘의 날씨 정보를 함께 저장하여 새 일정을 생성합니다.
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param todoSaveRequest 일정 생성 요청
	 * @return 생성된 일정 응답
	 */
	@Transactional
	public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
		User user = User.fromAuthUser(authUser);

		String weather = weatherClient.getTodayWeather();

		Todo newTodo = new Todo(
			todoSaveRequest.getTitle(),
			todoSaveRequest.getContents(),
			weather,
			user
		);
		Todo savedTodo = todoRepository.save(newTodo);

		return new TodoSaveResponse(
			savedTodo.getId(),
			savedTodo.getTitle(),
			savedTodo.getContents(),
			weather,
			new UserResponse(user.getId(), user.getEmail(), user.getNickname())
		);
	}

	/**
	 * 날씨와 수정일 기간 조건을 선택적으로 적용해 일정 목록을 조회합니다.
	 *
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @param weather 날씨 조건
	 * @param createdAt 수정일 시작 조건
	 * @param modifiedAt 수정일 종료 조건
	 * @return 일정 응답 페이지
	 */
	public Page<TodoResponse> getTodos(
		int page, int size, String weather, LocalDateTime createdAt, LocalDateTime modifiedAt
	) {
		Pageable pageable = PageRequest.of(page - 1, size);

		Page<Todo> todos = todoRepository.searchTodos(weather, createdAt, modifiedAt, pageable);

		return todos.map(todo -> new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getContents(),
			todo.getWeather(),
			new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(), todo.getUser().getNickname()),
			todo.getCreatedAt(),
			todo.getModifiedAt()
		));
	}

	/**
	 * QueryDSL 기반 검색 조건으로 일정 검색 결과를 조회합니다.
	 *
	 * @param title 제목 검색어
	 * @param createdFrom 생성일 시작 조건
	 * @param createdTo 생성일 종료 조건
	 * @param managerNickname 담당자 닉네임 검색어
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 일정 검색 결과 페이지
	 */
	public Page<TodoSearchResponse> searchTodos(
		String title, LocalDateTime createdFrom, LocalDateTime createdTo, String managerNickname, int page, int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size);
		return todoRepository.searchTodosWithQueryDsl(
			title,
			createdFrom,
			createdTo,
			managerNickname,
			pageable
		);
	}

	/**
	 * 작성자 정보를 함께 조회하여 일정 단건 응답을 생성합니다.
	 *
	 * @param todoId 조회할 일정 ID
	 * @return 일정 단건 응답
	 */
	public TodoResponse getTodo(long todoId) {
		Todo todo = todoRepository.findByIdWithUser(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));

		User user = todo.getUser();

		return new TodoResponse(
			todo.getId(),
			todo.getTitle(),
			todo.getContents(),
			todo.getWeather(),
			new UserResponse(user.getId(), user.getEmail(), user.getNickname()),
			todo.getCreatedAt(),
			todo.getModifiedAt()
		);
	}
}
