package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 담당자 엔티티의 기본 CRUD와 일정별 담당자 조회 기능을 제공하는 Repository입니다.
 */
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    /**
     * 담당자 사용자를 함께 fetch join하여 일정의 담당자 목록을 조회합니다.
     *
     * @param todoId 담당자를 조회할 일정 ID
     * @return 사용자 정보가 함께 로딩된 담당자 목록
     */
    @Query("SELECT m FROM Manager m JOIN FETCH m.user WHERE m.todo.id = :todoId")
    List<Manager> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
