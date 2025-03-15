package carrotmoa.carrotmoa.util;

import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.HashMap;
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
     * Get Access Token (재시도 없음)
     */
    public Map<String, Object> getAccessToken() {
        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();

        Map<String, Object> requestBody = Map.of("imp_key", impKey, "imp_secret", impSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


//        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "강제 5XX 에러 발생");


        // ✅ 예외가 발생하면 `cancelPayment()`에서 처리
        return restClient.post()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .body(requestBody)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * Cancel Payment (재시도 가능)
     */
    @Retryable(
            retryFor = {ResourceAccessException.class, HttpServerErrorException.class},
            noRetryFor = {HttpClientErrorException.class, UnknownHttpStatusCodeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public String cancelPayment(String impUid) {
        try {
            Map<String, Object> accessTokenResponse = getAccessToken();
            Map<String, Object> responseMap = (Map<String, Object>) accessTokenResponse.get("response");
            String accessToken = (String) responseMap.get("access_token");

            String url = BASE_URL + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl();
            Map<String, Object> requestBody = Map.of("imp_uid", impUid);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            return restClient.post()
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

        } catch (HttpClientErrorException httpEx) {
            log.error("❌ [cancelPayment] 즉시 실패 - 4XX 오류 발생. impUid: {}, Status: {}, Message: {}",
                    impUid, httpEx.getStatusCode(), httpEx.getMessage());
            throw httpEx;
        } catch (UnknownHttpStatusCodeException unknownStatusEx) {
            log.error("❌ [cancelPayment] 즉시 실패 - 알 수 없는 상태 코드. impUid: {}, Message: {}",
                    impUid, unknownStatusEx.getMessage());
            throw unknownStatusEx;
        } catch (ResourceAccessException | HttpServerErrorException retryableEx) {
            log.warn("⚠️ [cancelPayment] 재시도 가능 - 네트워크 오류 또는 5XX 발생. impUid: {}, Message: {}",
                    impUid, retryableEx.getMessage());
            throw retryableEx; // @Retryable 적용됨
        } catch (RestClientException unexpectedEx) {
            log.error("🚨 [cancelPayment] 예상치 못한 예외 발생. impUid: {}, Message: {}",
                    impUid, unexpectedEx.getMessage());
            throw unexpectedEx;
        }
    }

    /**
     * 공통 예외 처리
     */
    private static void handleRestClientException(RestClientException e, String methodName, String impUid) {
        String impUidLog = (impUid != null) ? impUid : "N/A";

        if (e instanceof HttpClientErrorException httpEx) {
            log.error("❌ [{}] 즉시 실패 - 4XX 오류 발생. impUid: {}, Status: {}, Message: {}",
                    methodName, impUidLog, httpEx.getStatusCode(), httpEx.getMessage());
        } else if (e instanceof UnknownHttpStatusCodeException) {
            log.error("❌ [{}] 즉시 실패 - 알 수 없는 상태 코드. impUid: {}, Message: {}",
                    methodName, impUidLog, e.getMessage());
        } else if (e instanceof ResourceAccessException || e instanceof HttpServerErrorException) {
            log.warn("⚠️ [{}] 재시도 가능 - 네트워크 오류 또는 5XX 발생. impUid: {}, Message: {}",
                    methodName, impUidLog, e.getMessage());
        } else {
            log.error("🚨 [{}] 예상치 못한 예외 발생. impUid: {}, Message: {}", methodName, impUidLog, e.getMessage());
        }
    }
}
