package org.example.expert.domain.log.repository;

import org.example.expert.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 요청 로그 엔티티의 저장과 조회를 담당하는 Repository입니다.
 */
public interface LogRepository extends JpaRepository<Log, Long> {

}
