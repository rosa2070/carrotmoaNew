package carrotmoa.carrotmoa.config.security;

import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.model.response.UserLoginResponse;
import carrotmoa.carrotmoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Value("${spring.user.profile.default-image}")
    private String defaultProfileImageUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 🔹 User 엔티티 → Response 객체 변환
        UserLoginResponse userLoginResponse = new UserLoginResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getIsWithdrawal(),
                user.getAuthorityId(),
                user.getState()
        );

        // 🔹 User만 넘겨주고, Repository는 CustomUserDetails 내부에서 Lazy Injection
        return new CustomUserDetails(userLoginResponse, defaultProfileImageUrl);
    }
}
