package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.Account;
import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.entity.UserProfile;
import lombok.Data;

@Data
public class UserJoinDto {
    private String email;
    private String password;
    private String nickname;
    private Long authorityId;
    private Boolean isWithdrawal = false;
    private String bankName;
    private Integer accountNumber;
    private String accountHolder;
    private long userId;
    private int state = 0;

    public User toUserEntity() {
        return User.builder()
            .email(this.getEmail())
            .password(this.getPassword())
            .authorityId(this.getAuthorityId())
            .isWithdrawal(this.getIsWithdrawal())
            .state(this.getState())
            .build();
    }

    public UserProfile toUserProfileEntity(long userId) {
        return UserProfile.builder()
            .userId(userId)
            .nickname(this.getNickname())
            .build();
    }

    public Account toHostAdditionalFormEntity(long userId) {
        return carrotmoa.carrotmoa.entity.Account.builder()
            .userId(userId)
            .bankName(this.getBankName())
            .accountNumber(this.getAccountNumber())
            .accountHolder(this.getAccountHolder())
            .build();
    }
}

