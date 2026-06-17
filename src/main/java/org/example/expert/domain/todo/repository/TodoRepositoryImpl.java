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

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private static final QManager managerForCount = new QManager("managerForCount");
	private static final QManager managerForSearch = new QManager("managerForSearch");
	private static final QUser managerUser = new QUser("managerUser");
	private static final QComment commentForCount = new QComment("commentForCount");

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		Todo foundTodo = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user).fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne();

		return Optional.ofNullable(foundTodo);
	}

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

	private BooleanExpression titleContains(String title) {
		return StringUtils.hasText(title) ? todo.title.contains(title) : null;
	}

	private BooleanExpression createdAtGoe(LocalDateTime createdFrom) {
		return createdFrom != null ? todo.createdAt.goe(createdFrom) : null;
	}

	private BooleanExpression createdAtLoe(LocalDateTime createdTo) {
		return createdTo != null ? todo.createdAt.loe(createdTo) : null;
	}

	private BooleanExpression managerNicknameContains(String managerNickname) {
		return StringUtils.hasText(managerNickname)
			? managerUser.nickname.contains(managerNickname)
			: null;
	}
}
