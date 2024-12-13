package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthResponse: API 응답의 외부 구조 (code, message, response)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Integer code; // 응답 코드 (Optional)
    private String message; // 응답 메시지 (Optional)
    private AuthAnnotation response; // 실제 데이터 (access_token 등)
}
