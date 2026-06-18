package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 엔티티의 기본 CRUD와 인증, 닉네임 검색 기능을 제공하는 Repository입니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자를 조회합니다.
     *
     * @param email 조회할 이메일
     * @return 이메일에 해당하는 사용자
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이미 존재하면 true
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임과 정확히 일치하는 사용자를 엔티티로 조회합니다.
     *
     * @param nickname 조회할 닉네임
     * @return 닉네임이 일치하는 사용자 목록
     */
    List<User> findByNickname(String nickname);

    /**
     * 닉네임과 정확히 일치하는 사용자를 DTO Projection으로 조회합니다.
     *
     * @param nickname 조회할 닉네임
     * @return 검색 응답 DTO 목록
     */
    @Query("""
        SELECT new org.example.expert.domain.user.dto.response.UserSearchResponse(
            u.id,
            u.email,
            u.nickname
        )
        FROM User u
        WHERE u.nickname = :nickname
    """)
    List<UserSearchResponse> searchByNickname(@Param("nickname") String nickname);
}
