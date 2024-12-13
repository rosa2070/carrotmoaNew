package carrotmoa.carrotmoa.util;

import java.util.*;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import carrotmoa.carrotmoa.model.response.AuthResponse;
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
        this.restClient = builder.baseUrl(baseUrl).build();
    }

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
    public AuthResponse getAccessToken() {
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
                    .body(AuthResponse.class);

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
        AuthResponse authResponse = getAccessToken();
        String accessToken = authResponse.getResponse().getAccess_token();

        String url = baseUrl + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();
        try {
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

}