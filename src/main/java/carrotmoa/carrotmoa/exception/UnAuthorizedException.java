package carrotmoa.carrotmoa.exception;

import org.springframework.http.HttpHeaders;

public class UnAuthorizedException extends ClientErrorException {
    public UnAuthorizedException(HttpHeaders headers, String message) {
        super(401, headers, message);
    }
}
