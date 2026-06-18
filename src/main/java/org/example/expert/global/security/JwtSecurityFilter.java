package org.example.expert.global.security;

import java.io.IOException;
import java.util.List;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.jwt.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		String url = request.getRequestURI();

		if (
			url.startsWith("/auth") ||
				url.equals("/chat.html") ||
				url.startsWith("/ws/chat")
		) {
			filterChain.doFilter(request, response);
			return;
		}

		String bearerJwt = request.getHeader("Authorization");

		if (bearerJwt == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
			return;
		}

		try {
			String jwt = jwtUtil.substringToken(bearerJwt);
			Claims claims = jwtUtil.extractClaims(jwt);

			Long userId = Long.parseLong(claims.getSubject());
			String email = claims.get("email", String.class);
			String userRole = claims.get("userRole", String.class);
			String nickname = claims.get("nickname", String.class);

			AuthUser authUser = new AuthUser(
				userId,
				email,
				UserRole.of(userRole),
				nickname
			);

			List<GrantedAuthority> authorities = List.of(
				new SimpleGrantedAuthority("ROLE_" + userRole)
			);

			Authentication authentication =
				new UsernamePasswordAuthenticationToken(authUser, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
		} catch (JwtException | IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");
		}
	}
}
