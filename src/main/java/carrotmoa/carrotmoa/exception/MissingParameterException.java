package carrotmoa.carrotmoa.exception;


import org.springframework.http.HttpHeaders;

public class MissingParameterException extends ClientErrorException {
    public MissingParameterException(HttpHeaders headers, String message) {
        super(400, headers, message);
    }

}
