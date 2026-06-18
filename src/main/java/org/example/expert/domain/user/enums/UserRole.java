package org.example.expert.domain.user.enums;

import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

/**
 * 사용자의 권한 종류를 나타내는 enum입니다.
 */
public enum UserRole {
    ADMIN, USER;

    /**
     * 문자열로 전달된 권한 값을 UserRole로 변환합니다.
     *
     * @param role 변환할 권한 문자열
     * @return 변환된 사용자 권한
     */
    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
    }
}
