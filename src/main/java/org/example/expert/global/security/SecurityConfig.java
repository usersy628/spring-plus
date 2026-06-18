package org.example.expert.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * JWT 인증을 사용하는 Spring Security 설정 클래스입니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtSecurityFilter jwtSecurityFilter;

	/**
	 * 인증이 필요 없는 경로와 관리자 권한 경로를 구분하고 JWT 필터를 등록합니다.
	 *
	 * @param http Spring Security HTTP 설정 객체
	 * @return 애플리케이션에 적용할 보안 필터 체인
	 * @throws Exception 보안 설정 구성 중 발생할 수 있는 예외
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers("/chat.html").permitAll()
				.requestMatchers("/ws/chat/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}
