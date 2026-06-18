package org.example.expert.domain.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 닉네임 검색 성능 비교를 위해 대량 사용자 데이터를 생성하는 수동 테스트입니다.
 */
@SpringBootTest
class UserBulkInsertTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * users 테이블에 100만 건의 테스트 데이터를 batch insert로 생성합니다.
	 */
	@Disabled("대용량 데이터 생성용 테스트 - 필요 시 수동 실행")
	@Test
	void 유저_100만건_생성() {
		int totalCount = 1_000_000;
		int batchSize = 5_000;

		String sql = """
            INSERT INTO users (email, password, user_role, nickname, created_at, modified_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

		for (int start = 0; start < totalCount; start += batchSize) {
			List<Object[]> batchArgs = new ArrayList<>();

			for (int i = start; i < start + batchSize && i < totalCount; i++) {
				String nickname = "nick_" + i;
				batchArgs.add(new Object[]{
					"bulk" + i + "@test.com",
					"password",
					"USER",
					nickname,
					LocalDateTime.now(),
					LocalDateTime.now()
				});
			}

			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}
}
