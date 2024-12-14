package carrotmoa.carrotmoa.exception;

import lombok.Getter;
import org.springframework.http.HttpHeaders;

@Getter
public class ClientErrorException extends RuntimeException {
    private int statusCode;
    private HttpHeaders headers;

    // 기본 생성자 (기본 메시지 포함)
    public ClientErrorException(int statusCode, HttpHeaders headers) {
        super("Client error occurred with status code: " + statusCode);
        this.statusCode = statusCode;
        this.headers = headers;
    }

    // 메시지를 매개변수로 받는 생성자
    public ClientErrorException(int statusCode, HttpHeaders headers, String message) {
        super(message != null ? message : "Client error occurred with status code: " + statusCode);
        this.statusCode = statusCode;
        this.headers = headers;
    }
}
