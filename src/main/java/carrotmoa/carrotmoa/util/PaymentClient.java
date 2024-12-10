package carrotmoa.carrotmoa.util;

import java.util.*;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestClient restClient;

    @Value("${payment.imp-key}")
    private String impKey;

    @Value("${payment.imp-secret}")
    private String impSecret;

    private static final String BASE_URL = "https://api.iamport.kr";

    /**
     * Get Access Token
     *
     * @return Access token as String
     */
    @Retryable(
            retryFor = {RestClientException.class}, // 이 예외가 발생하면 재시도
            maxAttempts = 3, // 최대 3번 재시도
            backoff = @Backoff(delay = 2000) // 재시도 간 대기시간 2초
    )
    public Map<String, Object> getAccessToken() {
        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
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
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    /**
     * Cancel Payment
     * @param impUid imp_uid of the payment to cancel
     * @return Response from PortOne API
     */
    public String cancelPayment(String impUid) {
        String accessToken = ((HashMap<?, ?>)getAccessToken().get("response")).get("access_token").toString();

        String url = BASE_URL + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();
        try {
            // Construct the request body for payment cancellation

            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("imp_uid", impUid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Send POST request
            return restClient
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(requestMap)
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to cancel payment", e);
        }
    }

    /**
     * Create Payment (Example: if you want to add payment creation)
     *
     * @param paymentRequest Payment request data
     * @param token          Access token
     * @return Response from PortOne API
     */
    public String createPayment(String paymentRequest, String token) {
        String url = BASE_URL + PortOneRequestUrl.CREATE_PAYMENT_URL.getUrl();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            // Send POST request
            return restClient
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(paymentRequest)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to create payment", e);
        }
    }
}