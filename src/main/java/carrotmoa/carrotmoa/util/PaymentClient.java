package carrotmoa.carrotmoa.util;

import java.util.LinkedHashMap;
import java.util.Map;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private static final String BASE_URL = "https://api.iamport.kr";

//    private final RestClient restClient;

    private final WebClient.Builder webClientBuilder;

    @Value("${payment.imp-key}")
    private String impKey;

    @Value("${payment.imp-secret}")
    private String impSecret;

    /**
     * Get Access Token
     *
     * @return Access token as String
     */
    @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Map getAccessToken() {
        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
        try {
            String requestBody = String.format("{\"imp_key\": \"%s\", \"imp_secret\": \"%s\"}", impKey, impSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Send POST request
            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();  // 응답을 기다림
        } catch (WebClientException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    /**
     * Cancel Payment
     *
     * @param impUid imp_uid of the payment to cancel
     * @return Response from PortOne API
     */
    @Retryable(retryFor = {WebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String cancelPayment(String impUid) {
        String accessToken = ((LinkedHashMap) getAccessToken().get("response")).get("access_token").toString();

        String url = BASE_URL + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();
        try {
            // Construct the request body for payment cancellation
            String requestBody = String.format("{\"imp_uid\": \"%s\"}", impUid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientException e) {
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
    @Retryable(retryFor = {WebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public String createPayment(String paymentRequest, String token) {
        String url = BASE_URL + PortOneRequestUrl.CREATE_PAYMENT_URL.getUrl();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            // Send POST request
            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .bodyValue(paymentRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientException e) {
            throw new RuntimeException("Failed to create payment", e);
        }
    }
}