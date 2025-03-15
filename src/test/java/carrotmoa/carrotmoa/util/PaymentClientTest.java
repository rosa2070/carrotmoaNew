//package carrotmoa.carrotmoa.util;
//
//import carrotmoa.carrotmoa.enums.PortOneRequestUrl;
//import carrotmoa.carrotmoa.exception.UnAuthorizedException;
//import carrotmoa.carrotmoa.model.response.AuthResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClientException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
//
//@RestClientTest(PaymentClient.class)
//@TestPropertySource(locations = "classpath:application-test.properties")  // 테스트용 프로퍼티 파일 지정
//public class PaymentClientTest {
//
//    @Autowired
//    private PaymentClient paymentClient;
//
//    @Autowired
//    private MockRestServiceServer mockServer;  // Mock HTTP server
//
//    @Value("${payment.base-url}")
//    private String baseUrl;
//
//    @BeforeEach
//    public void setUp() {
//        // Mock 서버 초기화
//        mockServer.reset();
//    }
//
//    @DisplayName("Access Token 발급하기")
//    @Test
//    public void testGetAccessToken() {
//        // given
//        String impKey = "mock_imp_key";
//        String impSecret = "mock_imp_secret";
//
//        String expectedResponse = """
//                {
//                    "code": 0,
//                    "message": null,
//                    "response": {
//                        "access_token": "mock_access_token",
//                        "now": 1734089469,
//                        "expired_at": 1734089894
//                    }
//                }
//                """;
//
//        String expectedRequestBody = """
//                {
//                    "imp_key": "mock_imp_key",
//                    "imp_secret": "mock_imp_secret"
//                }
//                """;
//
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))  // POST 메서드 확인
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // JSON 요청 확인
//                .andExpect(content().json(expectedRequestBody))  // 요청 본문이 예상한 JSON과 일치하는지 확인
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(expectedResponse));
//
//
//        System.out.println("Expected Request Body: " + expectedRequestBody);  // 요청 본문 출력
//        System.out.println("Expected Response Body: " + expectedResponse);  // 응답 본문 출력
//
//        // when
//        AuthResponse authResponse = paymentClient.getAccessToken(impKey, impSecret);
//
//        // then
//        assertThat(authResponse).isNotNull();
//        assertThat(authResponse.getCode()).isEqualTo(0);
//        assertThat(authResponse.getResponse().getAccess_token()).isEqualTo("mock_access_token");
//
//        // mockServer와의 상호작용이 제대로 이루어졌는지 검증
//        mockServer.verify();
//    }
//
//    @DisplayName("잘못된 API 키와 시크릿으로 Access Token 요청 시 401 Unauthorized 응답 확인")
//    @Test
//    public void testGetAccessTokenWithInvalidCredentials() {
//        // given
//        String impKey = "wrong_imp_key";
//        String impSecret = "wrong_imp_secret";
//
//        // 예상되는 잘못된 응답 (401 Unauthorized)
//        String expectedErrorResponse = """
//                {
//                  "code": -1,
//                  "message": "인증에 실패하였습니다. API키와 secret을 확인하세요.",
//                  "response": null
//                }
//                """;
//
//        // 예상 요청 본문 (잘못된 imp_key와 imp_secret 포함)
//        String expectedRequestBody = """
//                {
//                    "imp_key": "wrong_imp_key",
//                    "imp_secret": "wrong_imp_secret"
//                }
//                """;
//
//        // mockServer는 잘못된 imp_key와 imp_secret을 사용한 요청에 대해 401 Unauthorized 응답을 시뮬레이션
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(expectedRequestBody))  // 잘못된 요청 본문 검증
//                .andRespond(withStatus(HttpStatus.UNAUTHORIZED)  // 401 Unauthorized 응답
//                        .body(expectedErrorResponse)  // 응답 본문 설정
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // when
//        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> {
//            paymentClient.getAccessToken(impKey, impSecret); // 잘못된 인증 정보로 호출
//        });
//
//        // then
//        assertThat(exception.getMessage()).isEqualTo("인증에 실패하였습니다. API키와 secret을 확인하세요.");
//        assertThat(exception.getHeaders()).isNotNull();
////        mockServer.verify();  // Mock 서버가 요청을 받았는지 검증
//    }
//
//    @DisplayName("결제 취소 성공")
//    @Test
//    public void testCancelPaymentSuccess() {
//        // given
//        String impUid = "imp_295745947742";
//        String tokenResponse = """
//                {
//                    "code": 0,
//                    "message": null,
//                    "response": {
//                        "access_token": "mock_access_token"
//                    }
//                }
//                """;
//
//        String cancelResponse = """
//                {
//                    "code": 0,
//                    "message": null,
//                    "response": {
//                        "imp_uid": "imp_295745947742",
//                        "cancel_amount": 2175,
//                        "status": "cancelled",
//                        "cancel_reason": "취소요청api"
//                    }
//                }
//                """;
//
//        // Mock 서버에서 access token 발급 응답
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withSuccess(tokenResponse, MediaType.APPLICATION_JSON));
//
//        // Mock cancelPayment에서 결제 취소 성공 응답
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))
//                .andExpect(header("Authorization", "Bearer mock_access_token"))
//                .andRespond(withSuccess(cancelResponse, MediaType.APPLICATION_JSON));
//
//
//        // when
//        String result = paymentClient.cancelPayment(impUid);
//
//        // then
//
//        assertThat(result).contains("cancelled");
//        assertThat(result).contains("2175"); // 취소된 금액
//        assertThat(result).contains("취소요청api"); // 취소 사유
//        assertThat(result).contains(impUid);
//    }
//
//    @DisplayName("Payment 취소 시 오류 발생")
//    @Test
//    public void testCancelPaymentWithError() {
//        // given
//        String impUid = "mock_imp_uid";
//        String tokenResponse = """
//                {
//                    "code": 0,
//                    "message": null,
//                    "response": {
//                        "access_token": "mock_access_token"
//                    }
//                }
//                """;
//
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))
//                .andRespond(withSuccess(tokenResponse, MediaType.APPLICATION_JSON));
//
//        // Mock cancelPayment에서 400 에러 응답
//        mockServer.expect(requestTo(baseUrl + PortOneRequestUrl.CANCEL_PAYMENT_URL.getUrl()))
//                .andExpect(method(HttpMethod.POST))
//                .andExpect(header("Authorization", "Bearer mock_access_token"))
//                .andRespond(withStatus(HttpStatus.BAD_REQUEST));  // 400 Bad Request
//
//        // when & then
//        try {
//            paymentClient.cancelPayment(impUid);
//        } catch (RuntimeException e) {
//            assertThat(e.getMessage()).contains("Failed to cancel payment");
//        }
//    }
//}
