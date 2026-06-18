package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 일정 엔티티의 기본 CRUD와 과제용 검색 기능을 제공하는 Repository입니다.
 */
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

	/**
	 * 날씨와 수정일 기간 조건을 선택적으로 적용해 일정을 검색합니다.
	 *
	 * @param weather 날씨 조건
	 * @param createdAt 수정일 시작 조건
	 * @param modifiedAt 수정일 종료 조건
	 * @param pageable 페이지 요청 정보
	 * @return 수정일 내림차순으로 정렬된 일정 페이지
	 */
	@Query(
		value = """
			SELECT t
			FROM Todo t
			LEFT JOIN FETCH t.user
			WHERE (:weather IS NULL OR t.weather = :weather)
			  AND (:createdAt IS NULL OR t.modifiedAt >= :createdAt)
			  AND (:modifiedAt IS NULL OR t.modifiedAt <= :modifiedAt)
			ORDER BY t.modifiedAt DESC
			""",
		countQuery = """
			SELECT COUNT(t)
			FROM Todo t
			WHERE (:weather IS NULL OR t.weather = :weather)
			  AND (:createdAt IS NULL OR t.modifiedAt >= :createdAt)
			  AND (:modifiedAt IS NULL OR t.modifiedAt <= :modifiedAt)
			"""
	)
	Page<Todo> searchTodos(
		@Param("weather") String weather,
		@Param("createdAt") LocalDateTime createdAt,
		@Param("modifiedAt") LocalDateTime modifiedAt,
		Pageable pageable
	);
}
