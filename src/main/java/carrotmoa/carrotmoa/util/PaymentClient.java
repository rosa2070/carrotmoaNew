package carrotmoa.carrotmoa.util;

import java.time.Duration;
import java.util.*;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import carrotmoa.carrotmoa.exception.ClientErrorException;
import carrotmoa.carrotmoa.exception.MissingParameterException;
import carrotmoa.carrotmoa.exception.UnAuthorizedException;
import carrotmoa.carrotmoa.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClient {

    private final RestClient restClient;

    @Value("${payment.imp-key}")
    private String impKey;

    @Value("${payment.imp-secret}")
    private String impSecret;

    private static final String BASE_URL = "https://api.iamport.kr";

    /**
     * Get Access Token
     * @return Access token as String
     */
    public Map<String, Object> getAccessToken() {
        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
        try {

            Map<String, Object> requestBody = Map.of("imp_key", impKey, "imp_secret", impSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 강제로 500 Internal Server Error 발생
//            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "강제 500 Internal Server Error 발생");

            // Send POST request
            return restClient
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    /**
     * Cancel Payment
     * @param impUid imp_uid of the payment to cancel
     * @return Response from PortOne API
     */
    @Retryable(
            retryFor = {RestClientException.class},  // 네트워크 및 5XX 오류는 재시도
            noRetryFor = {HttpClientErrorException.class}, // 400번대 오류는 재시도 안 함
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2) // 초기 1초, 이후 2배씩 증가 (1s → 2s → 4s)
    )
    public String cancelPayment(String impUid) {
        try {
            // Access Token 가져오기
            Map<String, Object> accessTokenResponse = getAccessToken();
            Map<String, Object> responseMap = (Map<String, Object>) accessTokenResponse.get("response");
            String accessToken = (String) responseMap.get("access_token");

            // 요청 URL 설정
            String url = BASE_URL + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();

            // 요청 본문 생성
            Map<String, Object> requestBody = Map.of("imp_uid", impUid);

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            return restClient.post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            log.error("4XX 오류 발생 - 재시도하지 않음. impUid: {}, Status: {}", impUid, e.getStatusCode());
            throw e;
        } catch (RestClientException e) {
            log.error("5XX 또는 네트워크 오류 발생 - 재시도 가능. impUid: {}", impUid);
            throw e;
        }
    }

    @Recover
    public String handlePaymentFailure(RestClientException e, String impUid) {
        log.error("결제 취소 API 3회 재시도 후 실패. impUid: {}", impUid);
        throw e;
    }



    // getAccessToken() 예제 응답
    // {
    //    "code": 0,
    //    "message": "success",
    //    "response": {
    //        "access_token": "your_access_token",
    //        "expired_at": 1700003600
    //    }
    //}





}