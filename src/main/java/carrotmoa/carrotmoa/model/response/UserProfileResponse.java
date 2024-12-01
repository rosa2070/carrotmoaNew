package carrotmoa.carrotmoa.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String bio;
    private String picUrl;

    public static UserProfileResponse fromData(Object[] data) {
        return UserProfileResponse.builder()
            .id((Long) data[0])
            .userId((Long) data[1])
            .nickname((String) data[2])
            .bio((String) data[3])
            .picUrl((String) data[4])
            .build();
    }
}
