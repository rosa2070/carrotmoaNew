package carrotmoa.carrotmoa.config.security;

import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.enums.AuthorityCode;
import carrotmoa.carrotmoa.model.response.UserAddressResponse;
import carrotmoa.carrotmoa.model.response.UserLoginResponse;
import carrotmoa.carrotmoa.model.response.UserLoginResponseDto;
import carrotmoa.carrotmoa.repository.UserAddressRepository;
import carrotmoa.carrotmoa.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UserLoginResponse user;

    // 🔹 Repository는 직렬화되지 않도록 transient 적용
    private transient UserProfileRepository userProfileRepository;
    private transient UserAddressRepository userAddressRepository;

    private final String defaultProfileImageUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthorityCode.getAuthorityCodeName(user.getAuthorityId())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 🔹 Lazy Dependency Injection을 이용한 동적 주입
    private UserProfileRepository getUserProfileRepository() {
        if (userProfileRepository == null) {
            userProfileRepository = ApplicationContextProvider.getApplicationContext().getBean(UserProfileRepository.class);
        }
        return userProfileRepository;
    }

    private UserAddressRepository getUserAddressRepository() {
        if (userAddressRepository == null) {
            userAddressRepository = ApplicationContextProvider.getApplicationContext().getBean(UserAddressRepository.class);
        }
        return userAddressRepository;
    }

    public UserLoginResponseDto getUserProfile() {
        if (user == null) {
            throw new IllegalStateException("User object is null in CustomUserDetails.");
        }

        if (user.getId() == null) {
            throw new IllegalStateException("User ID is null in CustomUserDetails. Possible session issue.");
        }

        var profile = getUserProfileRepository().findByUserId(user.getId());
        if (profile == null) {
            throw new IllegalArgumentException("User profile not found for userId: " + user.getId());
        }

        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(
                user,
                Objects.requireNonNull(getUserProfileRepository().findByUserId(user.getId()))
        );

        if (userLoginResponseDto.getPicUrl() == null) {
            userLoginResponseDto.setPicUrl(defaultProfileImageUrl);
        }
        return userLoginResponseDto;
    }

    public UserAddressResponse getUserAddress() {
        if (getUserAddressRepository().findByUserId(user.getId()) != null) {
            return new UserAddressResponse(getUserAddressRepository().findByUserId(user.getId()));
        } else {
            return new UserAddressResponse();
        }
    }

    public String getUserAuthority() {
        return AuthorityCode.getAuthorityCodeName(user.getAuthorityId());
    }
}
