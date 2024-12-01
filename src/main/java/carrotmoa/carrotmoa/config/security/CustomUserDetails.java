package carrotmoa.carrotmoa.config.security;

import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.enums.AuthorityCode;
import carrotmoa.carrotmoa.model.response.UserAddressResponse;
import carrotmoa.carrotmoa.model.response.UserLoginResponseDto;
import carrotmoa.carrotmoa.repository.UserAddressRepository;
import carrotmoa.carrotmoa.repository.UserProfileRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;
    private final UserProfileRepository userprofileRepository;
    private final UserAddressRepository useraddressRepository;
    private final String defaultProfileImageUrl;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
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

    public UserLoginResponseDto getUserProfile() {
        UserLoginResponseDto userLoginResponseDto
                = new UserLoginResponseDto(user, Objects.requireNonNull(userprofileRepository.findByUserId(user.getId())));

        if (userLoginResponseDto.getPicUrl() == null) {
            userLoginResponseDto.setPicUrl(defaultProfileImageUrl);
        }
        return userLoginResponseDto;
    }

    public UserAddressResponse getUserAddress() {
        if(useraddressRepository.findByUserId(user.getId()) != null){
        return new UserAddressResponse(useraddressRepository.findByUserId(user.getId()));
        } else {
            return new UserAddressResponse();
        }
    }

    public String getUserAuthority() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(AuthorityCode.getAuthorityCodeName(user.getAuthorityId())));

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("NO_ROLE");
    }
}