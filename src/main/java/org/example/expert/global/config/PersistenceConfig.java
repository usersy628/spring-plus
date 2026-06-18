package org.example.expert.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

/**
 * JPA Auditing과 QueryDSL에서 사용할 영속성 관련 Bean을 설정합니다.
 */
@Configuration
@EnableJpaAuditing
public class PersistenceConfig {

	/**
	 * QueryDSL 쿼리 작성을 위한 JPAQueryFactory Bean을 등록합니다.
	 *
	 * @param entityManager JPA EntityManager
	 * @return QueryDSL JPAQueryFactory
	 */
	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
