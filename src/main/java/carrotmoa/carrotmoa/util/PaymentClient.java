package carrotmoa.carrotmoa.util;

import carrotmoa.carrotmoa.service.PortOneRequestUrl;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private static final String BASE_URL = "https://api.iamport.kr";
    private final RestClient restClient;
    @Value("${payment.imp-key}")
    private String impKey;
    @Value("${payment.imp-secret}")
    private String impSecret;

    /**
     * Get Access Token
     *
     * @return Access token as String
     */
    public Map getAccessToken() {
        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
        try {
            // Construct the request body
            String requestBody = String.format("{\"imp_key\": \"%s\", \"imp_secret\": \"%s\"}", impKey, impSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Send POST request
            return restClient
                .post()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    /**
     * Cancel Payment
     *
     * @param impUid imp_uid of the payment to cancel
     * @return Response from PortOne API
     */
    public String cancelPayment(String impUid) {
        String accessToken = ((LinkedHashMap) getAccessToken().get("response")).get("access_token").toString();

        String url = BASE_URL + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();
        try {
            // Construct the request body for payment cancellation
            String requestBody = String.format("{\"imp_uid\": \"%s\"}", impUid);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Send POST request
            return restClient
                .post()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .body(requestBody)
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