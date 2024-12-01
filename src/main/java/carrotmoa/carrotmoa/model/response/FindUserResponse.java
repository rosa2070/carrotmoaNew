package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindUserResponse {
    private long userId;
    private String nickname;

    public FindUserResponse(UserProfile profile) {
        this.userId = profile.getUserId();
        this.nickname = profile.getNickname();
    }

}
