package org.example.expert.domain.user.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.global.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 조회, 비밀번호 변경, 닉네임 검색 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 응답
     */
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }

    /**
     * 기존 비밀번호 확인과 새 비밀번호 정책 검증 후 비밀번호를 변경합니다.
     *
     * @param userId 비밀번호를 변경할 사용자 ID
     * @param userChangePasswordRequest 비밀번호 변경 요청
     */
    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    /**
     * 새 비밀번호가 길이, 숫자, 대문자 조건을 만족하는지 검증합니다.
     *
     * @param userChangePasswordRequest 비밀번호 변경 요청
     */
    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    /**
     * 닉네임과 정확히 일치하는 사용자를 DTO Projection으로 조회하고 조회 시간을 로그로 남깁니다.
     *
     * @param nickname 검색할 닉네임
     * @return 사용자 검색 응답 목록
     */
    public List<UserSearchResponse> searchUsersByNickname(String nickname) {

        long start = System.currentTimeMillis();

        List<UserSearchResponse> users = userRepository.searchByNickname(nickname);

        long end = System.currentTimeMillis();

        log.info("nickname search elapsed time: {}ms", end - start);

        return users;
    }
}
