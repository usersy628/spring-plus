package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

/**
 * 일정 검색 결과에서 제목, 담당자 수, 댓글 수만 반환하기 위한 DTO입니다.
 */
@Getter
public class TodoSearchResponse {

	private final String title;
	private final Long managerCount;
	private final Long commentCount;

	/**
	 * QueryDSL Projection 결과를 담을 일정 검색 응답을 생성합니다.
	 *
	 * @param title 일정 제목
	 * @param managerCount 담당자 수
	 * @param commentCount 댓글 수
	 */
	public TodoSearchResponse(String title, Long managerCount, Long commentCount) {
		this.title = title;
		this.managerCount = managerCount;
		this.commentCount = commentCount;
	}
}
