package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 댓글 엔티티의 기본 CRUD와 일정별 댓글 조회 기능을 제공하는 Repository입니다.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 댓글 작성자를 함께 fetch join하여 일정의 댓글 목록을 조회합니다.
     *
     * @param todoId 댓글을 조회할 일정 ID
     * @return 작성자 정보가 함께 로딩된 댓글 목록
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId")
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
