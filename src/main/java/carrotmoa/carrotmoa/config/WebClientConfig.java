package carrotmoa.carrotmoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)  // 연결 타임아웃을 5초로 설정합니다.
                .responseTimeout(Duration.ofSeconds(10)); // 응답 타임아웃을 10초로 설정합니다.

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));

    }
}

