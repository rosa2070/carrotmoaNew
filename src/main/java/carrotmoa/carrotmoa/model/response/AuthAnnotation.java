package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthAnnotation: 실제 데이터 (access_token, expired_at, now)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthAnnotation {
    private String access_token; // access token
    private int expired_at; // token 만료 시각
    private int now; // 현재 시각
}
