package carrotmoa.carrotmoa.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestClientConfig {

    private static final int CONNECT_TIMEOUT = 3000;  // 연결 타임아웃 (3초)
    private static final int RESPONSE_TIMEOUT = 10000; // 응답 타임아웃 (10초)

    @Bean
    public RestClient restClient() {
        // 타임아웃 설정 (HttpClient 5.2+ 최신 방식)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)   // ✅ 최신 방식
                .setResponseTimeout(RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS) // ✅ 최신 방식
                .build();

        // 커넥션 풀 설정
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        poolingConnManager.setDefaultMaxPerRoute(50);  // API별 최대 50개 연결
        poolingConnManager.setMaxTotal(200);           // 전체 최대 200개 연결

        // HttpClient 생성
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        // RestClient 생성
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }
}
