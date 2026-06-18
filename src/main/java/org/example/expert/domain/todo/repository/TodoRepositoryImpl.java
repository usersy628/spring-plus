package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.todo.entity.QTodo.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * TodoRepository의 QueryDSL 기반 커스텀 조회 기능을 구현한 클래스입니다.
 */
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	/**
	 * 담당자 수 집계와 담당자 닉네임 검색이 서로 영향을 주지 않도록 조인 별칭을 분리합니다.
	 */
	private static final QManager managerForCount = new QManager("managerForCount");
	private static final QManager managerForSearch = new QManager("managerForSearch");
	private static final QUser managerUser = new QUser("managerUser");
	private static final QComment commentForCount = new QComment("commentForCount");

	/**
	 * Todo 단건 조회 시 작성자 정보를 함께 fetch join하여 N+1 문제를 방지합니다.
	 *
	 * @param todoId 조회할 일정 ID
	 * @return 작성자 정보가 함께 조회된 일정
	 */
	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		Todo foundTodo = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user).fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne();

		return Optional.ofNullable(foundTodo);
	}

	/**
	 * 제목, 생성일 기간, 담당자 닉네임 조건으로 일정을 검색하고 담당자 수와 댓글 수를 함께 조회합니다.
	 *
	 * @param title 제목 검색어
	 * @param createdFrom 생성일 시작 조건
	 * @param createdTo 생성일 종료 조건
	 * @param managerNickname 담당자 닉네임 검색어
	 * @param pageable 페이지 요청 정보
	 * @return 조건에 맞는 일정 검색 결과 페이지
	 */
	@Override
	public Page<TodoSearchResponse> searchTodosWithQueryDsl(
		String title,
		LocalDateTime createdFrom,
		LocalDateTime createdTo,
		String managerNickname,
		Pageable pageable
	) {
		List<TodoSearchResponse> content = queryFactory
			.select(Projections.constructor(
				TodoSearchResponse.class,
				todo.title,
				managerForCount.id.countDistinct(),
				commentForCount.id.countDistinct()
			))
			.from(todo)
			.leftJoin(todo.managers, managerForCount)
			.leftJoin(todo.comments, commentForCount)
			.leftJoin(todo.managers, managerForSearch)
			.leftJoin(managerForSearch.user, managerUser)
			.where(
				titleContains(title),
				createdAtGoe(createdFrom),
				createdAtLoe(createdTo),
				managerNicknameContains(managerNickname)
			)
			.groupBy(todo.id, todo.title, todo.createdAt)
			.orderBy(todo.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(todo.id.countDistinct())
			.from(todo)
			.leftJoin(todo.managers, managerForSearch)
			.leftJoin(managerForSearch.user, managerUser)
			.where(
				titleContains(title),
				createdAtGoe(createdFrom),
				createdAtLoe(createdTo),
				managerNicknameContains(managerNickname)
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total == null ? 0 : total);
	}

	/**
	 * 제목 검색어가 있을 때만 제목 포함 조건을 생성합니다.
	 */
	private BooleanExpression titleContains(String title) {
		return StringUtils.hasText(title) ? todo.title.contains(title) : null;
	}

	/**
	 * 생성일 시작 조건이 있을 때만 생성일 이상 조건을 생성합니다.
	 */
	private BooleanExpression createdAtGoe(LocalDateTime createdFrom) {
		return createdFrom != null ? todo.createdAt.goe(createdFrom) : null;
	}

	/**
	 * 생성일 종료 조건이 있을 때만 생성일 이하 조건을 생성합니다.
	 */
	private BooleanExpression createdAtLoe(LocalDateTime createdTo) {
		return createdTo != null ? todo.createdAt.loe(createdTo) : null;
	}

	/**
	 * 담당자 닉네임 검색어가 있을 때만 닉네임 포함 조건을 생성합니다.
	 */
	private BooleanExpression managerNicknameContains(String managerNickname) {
		return StringUtils.hasText(managerNickname)
			? managerUser.nickname.contains(managerNickname)
			: null;
	}
}
