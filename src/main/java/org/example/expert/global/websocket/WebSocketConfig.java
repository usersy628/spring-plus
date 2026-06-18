package org.example.expert.global.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/chat") // WebSocket 연결 주소
			.setAllowedOriginPatterns("*")
			.withSockJS(); // SockJS transport용 임의 값 - ws://localhost:8080/ws/chat/123/test/websocket
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub"); // 클라이언트가 서버 메세지를 구독할 때 사용하는 prefix
		registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트가 서버로 메세지를 보낼 때 사용하는 prefix
	}
}
