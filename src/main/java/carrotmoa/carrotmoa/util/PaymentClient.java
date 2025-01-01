package carrotmoa.carrotmoa.util;

import java.time.Duration;
import java.util.*;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import carrotmoa.carrotmoa.exception.ClientErrorException;
import carrotmoa.carrotmoa.exception.MissingParameterException;
import carrotmoa.carrotmoa.exception.UnAuthorizedException;
import carrotmoa.carrotmoa.model.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
@Slf4j
//@RequiredArgsConstructor
public class PaymentClient {

    @Value("${payment.imp-key}")
    private String impKey;

    @Value("${payment.imp-secret}")
    private String impSecret;

    @Value("${payment.base-url}")
    private String baseUrl;

    private final RestClient restClient;

    public PaymentClient(RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Get Access Token
     *
     * @return Access token as String
     */
    public AuthResponse getAccessToken(String impKey, String impSecret) {
        // 테스트 시에 impKey, impSecret을 넘겨줄 수 있도록 하되, 값이 null인 경우에는 @Value에서 읽어온 값을 사용
        impKey = (impKey != null) ? impKey : this.impKey;
        impSecret = (impSecret != null) ? impSecret : this.impSecret;

        String url = baseUrl + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
        try {
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("imp_key", impKey);
            requestMap.put("imp_secret", impSecret);

            // Send POST request
            return restClient
                    .post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestMap)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        int statusCode = response.getStatusCode().value();
                        HttpHeaders headers = response.getHeaders();

                        // 상태 코드 출력 (로그로 확인)
                        log.info("Received HTTP status code: {}, {}", statusCode, headers);

                        // 각 상태 코드에 맞는 예외 던지기
                        switch (statusCode) {
                            case 400:
                                throw new MissingParameterException(headers, "imp_key, imp_secret 파라메터가 누락되었습니다.");  // 400 Bad Request
                            case 401:
                                throw new UnAuthorizedException(headers, "인증에 실패하였습니다. API키와 secret을 확인하세요.");  // 401 Unauthorized
                            default:
                                // 다른 4xx 오류는 기본 ClientErrorException 던지기
                                throw new ClientErrorException(statusCode, headers, "Client error occurred for status code: " + statusCode);
                        }
                    })
                    .body(AuthResponse.class);

        } catch (RestClientException e) {
            // 네트워크 오류나 서버 오류 등 예외 처리
            log.error("Failed to get access token after retries. Error: ", e);
            throw new RuntimeException("Failed to get access token", e);

        }
    }

    /**
     * Cancel Payment
     * @param impUid imp_uid of the payment to cancel
     * @return Response from PortOne API
     */
    @Retryable(
            retryFor = {RestClientException.class}, // 재시도할 예외 지정
            maxAttempts = 2, // 최초 호출 1회 + 재시도 1회
            backoff = @Backoff(delay = 1000) // 1초 대기 후 재시도
    )
    public String cancelPayment(String impUid) {
        AuthResponse authResponse = getAccessToken(impKey, impSecret);
        String accessToken = authResponse.getResponse().getAccess_token();

        String url = baseUrl + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();

        // 타임아웃 설정
        Duration timeout = Duration.ofSeconds(3);  // 5초로 설정

        // SimpleClientHttpRequestFactory 생성 및 타임아웃 설정
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) timeout.toMillis());
        factory.setReadTimeout((int) timeout.toMillis());

//        // RestClient를 직접 생성하여 HttpClient 설정
        RestClient restClientWithTimeout = restClient.mutate()
                .baseUrl(url)  // 기본 URL 설정
                .requestFactory(factory)  // 직접 ClientHttpRequestFactory 객체 전달
                .build();

        try {
            // 응답 시간 측정을 위한 시작 시간 기록
            long startTime = System.nanoTime();  // 요청 전 시간 기록

            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("imp_uid", impUid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Send POST request
            String response = restClientWithTimeout
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(requestMap)
                    .retrieve()
                    .body(String.class);

            // 응답 시간 계산 (nanoseconds -> milliseconds로 변환)
            long endTime = System.nanoTime();  // 응답 후 시간 기록
            long duration = (endTime - startTime) / 1_000_000;  // 밀리초로 변환

            // 로그에 응답 시간 출력
            log.info("Response time: {} ms", duration);

            return response;

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to cancel payment", e);
        }
    }

}