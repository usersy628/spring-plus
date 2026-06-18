package org.example.expert.domain.user.dto.response;

import lombok.Getter;

/**
 * 닉네임 검색 결과에서 필요한 사용자 정보만 반환하기 위한 DTO입니다.
 */
@Getter
public class UserSearchResponse {

	private final Long id;
	private final String email;
	private final String nickname;

	/**
	 * JPQL DTO Projection 결과를 담을 사용자 검색 응답을 생성합니다.
	 *
	 * @param id 사용자 ID
	 * @param email 사용자 이메일
	 * @param nickname 사용자 닉네임
	 */
	public UserSearchResponse(Long id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}
}
