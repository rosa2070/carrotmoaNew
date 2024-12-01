package carrotmoa.carrotmoa.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
//@EnableWebSocketSecurity 알아보기
public class ChattingConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //접속 url http 가 아닌 wc://localhost
        registry.addEndpoint("chat")
            //cors설정 나중에 url특정해서 닫을 것
            .setAllowedOriginPatterns("*")
            //하위 버전의 http에서도 작동
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //채팅방 구독 경로
        registry.enableSimpleBroker("/sub");
        //구독한 채팅방에서 메시지를 발행하는 경로
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
