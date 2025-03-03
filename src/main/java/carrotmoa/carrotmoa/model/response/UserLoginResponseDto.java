package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.entity.UserAddress;
import carrotmoa.carrotmoa.entity.UserProfile;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private long userId;
    private String name;
    private String nickname;
    private String picUrl;
    private long addressId;
    private String phoneNumber;
    private LocalDate birthday;
    private String bio;
    private String defaultProfileImageUrl;

    public UserLoginResponseDto(UserLoginResponse user, UserProfile userProfile) {
        this.userId = user.getId();
        this.nickname = userProfile.getNickname();

        if (userProfile.getPicUrl() != null) {
            this.picUrl = userProfile.getPicUrl();
        }

        if (userProfile.getPhoneNumber() != null) {
            this.phoneNumber = userProfile.getPhoneNumber();
        }

        if (userProfile.getBirthday() != null) {
            this.birthday = userProfile.getBirthday();
        }
        if (userProfile.getBio() != null) {
            this.bio = userProfile.getBio();
        }
        if (userProfile.getAddressId() != null) {
            this.addressId = userProfile.getAddressId();
        }
        if (user.getName() != null) {
            this.name = user.getName();
        }
        if (userProfile.getPhoneNumber() != null) {
            this.phoneNumber = userProfile.getPhoneNumber();
        }
    }

}