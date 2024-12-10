package carrotmoa.carrotmoa.config;


import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // 타임아웃 값 설정
    private static final int CONNECT_TIMEOUT = 5000;  // 5초 연결 타임아웃
    private static final int READ_TIMEOUT = 10000;    // 10초 읽기 타임아웃

    @Bean
    public RestClient restClient() {
        // ConnectionConfig 설정
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(CONNECT_TIMEOUT)) // 연결 타임아웃
                .setSocketTimeout(Timeout.ofMilliseconds(READ_TIMEOUT)) // 읽기 타이아웃
                .setTimeToLive(TimeValue.ofMinutes(10)) // 연결 유효 시간
                .build();

        // PoolingHttpClientConnectionManager 설정
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        poolingConnManager.setDefaultConnectionConfig(connectionConfig);

        // HttpClient를 생성하고 커넥션 매니저를 설정
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .build();

        // HttpClient를 요청 팩토리로 설정하고 RestClient 빌드
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))  // 요청 팩토리 설정
                .build();
    }
}