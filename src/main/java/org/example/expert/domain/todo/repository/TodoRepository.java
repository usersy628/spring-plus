package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

	@Query("SELECT t FROM Todo t " +
		"LEFT JOIN t.user " +
		"WHERE t.id = :todoId")
	Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

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
