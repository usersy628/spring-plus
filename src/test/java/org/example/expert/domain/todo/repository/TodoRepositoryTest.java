package org.example.expert.domain.todo.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.config.PersistenceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

/**
 * TodoRepository의 QueryDSL 기반 커스텀 조회 기능을 검증하는 테스트입니다.
 */
@DataJpaTest
@Import(PersistenceConfig.class)
class TodoRepositoryTest {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private TestEntityManager entityManager;

	/**
	 * 일정 단건 조회 시 작성자 정보가 함께 조회되는지 검증합니다.
	 */
	@Test
	void findByIdWithUser는_todo와_user를_함께_조회한다() {
		User user = new User("test@email.com", "password", UserRole.USER, "nickname");
		entityManager.persist(user);

		Todo todo = new Todo("title", "contents", "Sunny", user);
		entityManager.persist(todo);
		entityManager.flush();
		entityManager.clear();

		Optional<Todo> result = todoRepository.findByIdWithUser(todo.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getUser().getEmail()).isEqualTo("test@email.com");
	}
}
