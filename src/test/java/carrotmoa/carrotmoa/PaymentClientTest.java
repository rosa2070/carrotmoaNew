//package carrotmoa.carrotmoa;
//
//import carrotmoa.carrotmoa.util.PaymentClient;
//import okhttp3.mockwebserver.MockResponse;  // MockWebServer의 응답을 설정할 수 있는 클래스
//import okhttp3.mockwebserver.MockWebServer;  // MockWebServer를 사용하여 서버를 모킹하는 클래스
//import org.junit.jupiter.api.BeforeEach;  // 각 테스트 메소드 실행 전에 호출되는 메소드 정의
//import org.junit.jupiter.api.AfterEach;   // 각 테스트 메소드 실행 후에 호출되는 메소드 정의
//import org.junit.jupiter.api.Test;        // 테스트 메소드에 대한 어노테이션
//import org.springframework.web.reactive.function.client.WebClient;  // WebClient는 비동기 HTTP 요청을 보내는 클래스
//import org.springframework.web.reactive.function.client.WebClientResponseException;  // WebClient에서 발생할 수 있는 예외 클래스
//import static org.junit.jupiter.api.Assertions.*;  // 단정문(assertEquals, assertNotNull 등)을 위한 클래스
//
//import java.io.IOException;
//import java.util.Map;
//
//public class PaymentClientTest {
//
//    private MockWebServer mockWebServer;  // MockWebServer 인스턴스 선언
//    private PaymentClient paymentClient;  // 테스트 대상 PaymentClient 인스턴스 선언
//
//    // 각 테스트 메소드가 실행되기 전에 호출되는 메소드
//    @BeforeEach
//    void setUp() throws IOException {
//        // MockWebServer 초기화 및 시작
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();  // 서버 시작
//
//        // WebClient를 mockWebServer의 URL을 기반으로 생성
//        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(mockWebServer.url("/").toString());
//
//        // PaymentClient 인스턴스를 WebClient를 사용해 생성
//        paymentClient = new PaymentClient(webClientBuilder);
//    }
//
//    // 각 테스트 메소드 실행 후에 호출되는 메소드
//    @AfterEach
//    void tearDown() throws IOException {
//        // MockWebServer 종료
//        mockWebServer.shutdown();  // 서버 종료
//    }
//
//    // 정상적으로 액세스 토큰을 얻는 테스트
//    @Test
//    void testGetAccessToken_Success() {
//        // MockWebServer에 성공적인 응답 설정 (200 OK, 액세스 토큰 반환)
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 액세스 토큰 요청
//        Map<String, Object> response = paymentClient.getAccessToken();
//
//        // 응답이 null이 아니어야 하고, "access_token" 값이 "test_token"이어야 한다
//        assertNotNull(response);
//        assertEquals("test_token", ((Map) response.get("response")).get("access_token"));
//    }
//
//    // 결제 취소 성공 테스트
//    @Test
//    void testCancelPayment_Success() {
//        // 첫 번째 응답: 토큰 발급
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // 두 번째 응답: 결제 취소 성공
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"status\": \"success\", \"message\": \"Payment cancelled successfully\" }")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 결제 취소 요청
//        String impUid = "12345";
//        String response = paymentClient.cancelPayment(impUid);
//
//        // 응답이 null이 아니어야 하고, 응답에 "success"가 포함되어야 한다
//        assertNotNull(response);
//        assertTrue(response.contains("success"));
//    }
//
//    // 결제 취소 실패 (4xx 오류) 테스트
//    @Test
//    void testCancelPayment_Failure_4xxError() {
//        // 첫 번째 응답: 토큰 발급
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // 두 번째 응답: 결제 취소 실패 (401 Unauthorized)
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(401)
//                .setBody("{ \"error\": \"Unauthorized\" }")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 결제 취소 요청 (401 에러가 발생할 것으로 예상)
//        String impUid = "12345";
//        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
//            paymentClient.cancelPayment(impUid);  // 예외 발생을 기대
//        });
//
//        // 예외 발생 확인: 상태 코드가 401이어야 하고, 응답 본문에 "Unauthorized"가 포함되어야 한다
//        assertEquals(401, exception.getRawStatusCode());
//        assertTrue(exception.getResponseBodyAsString().contains("Unauthorized"));
//    }
//
//    // 결제 취소 실패 (5xx 서버 오류) 테스트
//    @Test
//    void testCancelPayment_Failure_5xxError() {
//        // 첫 번째 응답: 토큰 발급
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // 두 번째 응답: 결제 취소 실패 (500 Internal Server Error)
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(500)
//                .setBody("{ \"error\": \"Internal Server Error\" }")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 결제 취소 요청 (500 에러가 발생할 것으로 예상)
//        String impUid = "12345";
//        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
//            paymentClient.cancelPayment(impUid);  // 예외 발생을 기대
//        });
//
//        // 예외 발생 확인: 상태 코드가 500이어야 하고, 응답 본문에 "Internal Server Error"가 포함되어야 한다
//        assertEquals(500, exception.getRawStatusCode());
//        assertTrue(exception.getResponseBodyAsString().contains("Internal Server Error"));
//    }
//
//    // 결제 생성 성공 테스트
//    @Test
//    void testCreatePayment_Success() {
//        // 첫 번째 응답: 토큰 발급
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // 두 번째 응답: 결제 생성 성공
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"status\": \"success\", \"message\": \"Payment created successfully\" }")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 결제 생성 요청
//        String paymentRequest = "{ \"amount\": 1000, \"card_number\": \"1234-5678-9101-1121\" }";
//        String response = paymentClient.createPayment(paymentRequest, "test_token");
//
//        // 응답이 null이 아니어야 하고, 응답에 "success"가 포함되어야 한다
//        assertNotNull(response);
//        assertTrue(response.contains("success"));
//    }
//
//    // 결제 생성 실패 (400 오류) 테스트
//    @Test
//    void testCreatePayment_Failure() {
//        // 첫 번째 응답: 토큰 발급
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{ \"response\": { \"access_token\": \"test_token\" }}")
//                .addHeader("Content-Type", "application/json"));
//
//        // 두 번째 응답: 결제 생성 실패 (400 Bad Request)
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(400)
//                .setBody("{ \"error\": \"Bad Request\" }")
//                .addHeader("Content-Type", "application/json"));
//
//        // PaymentClient를 통해 결제 생성 요청 (400 에러가 발생할 것으로 예상)
//        String paymentRequest = "{ \"amount\": 1000, \"card_number\": \"1234-5678-9101-1121\" }";
//        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
//            paymentClient.createPayment(paymentRequest, "test_token");  // 예외 발생을 기대
//        });
//
//        // 예외 발생 확인: 상태 코드가 400이어야 하고, 응답 본문에 "Bad Request"가 포함되어야 한다
//        assertEquals(400, exception.getRawStatusCode());
//        assertTrue(exception.getResponseBodyAsString().contains("Bad Request"));
//    }
//}
