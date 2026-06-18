package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

/**
 * 애플리케이션 사용자를 나타내는 엔티티입니다.
 */
@Getter
@Entity
@NoArgsConstructor
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_nickname", columnList = "nickname")
    }
)
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String nickname;

    /**
     * 회원가입 시 사용할 사용자 엔티티를 생성합니다.
     *
     * @param email 사용자 이메일
     * @param password 암호화된 비밀번호
     * @param userRole 사용자 권한
     * @param nickname 사용자 닉네임
     */
    public User(String email, String password, UserRole userRole, String nickname) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    private User(Long id, String email, UserRole userRole, String nickname) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    /**
     * 인증 객체로부터 필요한 사용자 식별 정보만 가진 엔티티를 생성합니다.
     *
     * @param authUser 인증된 사용자 정보
     * @return 인증 정보 기반 사용자 엔티티
     */
    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(), authUser.getUserRole(), authUser.getNickname());
    }

    /**
     * 사용자 비밀번호를 변경합니다.
     *
     * @param password 새로 암호화된 비밀번호
     */
    public void changePassword(String password) {
        this.password = password;
    }

    /**
     * 사용자 권한을 변경합니다.
     *
     * @param userRole 새 사용자 권한
     */
    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
