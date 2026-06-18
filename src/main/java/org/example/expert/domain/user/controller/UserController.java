package org.example.expert.domain.user.controller;

import java.util.List;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 조회, 닉네임 검색, 비밀번호 변경 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 닉네임과 정확히 일치하는 사용자를 검색합니다.
	 *
	 * @param nickname 검색할 닉네임
	 * @return 사용자 검색 결과 목록
	 */
	@GetMapping("/users/search")
	public ResponseEntity<List<UserSearchResponse>> searchUsersByNickname(
		@RequestParam String nickname
	) {
		return ResponseEntity.ok(userService.searchUsersByNickname(nickname));
	}

	/**
	 * 사용자 ID로 단건 사용자를 조회합니다.
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 사용자 응답
	 */
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
		return ResponseEntity.ok(userService.getUser(userId));
	}

	/**
	 * 인증된 사용자의 비밀번호를 변경합니다.
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param userChangePasswordRequest 비밀번호 변경 요청
	 */
	@PutMapping("/users")
	public void changePassword(
		@AuthenticationPrincipal AuthUser authUser,
		@RequestBody UserChangePasswordRequest userChangePasswordRequest
	) {
		userService.changePassword(authUser.getId(), userChangePasswordRequest);
	}
}
