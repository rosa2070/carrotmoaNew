package carrotmoa.carrotmoa.config.security;

import carrotmoa.carrotmoa.enums.AuthorityCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService detailService;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/service/**").authenticated()
                .requestMatchers("/admin/**").hasRole(AuthorityCode.ADMIN.name())
                .requestMatchers("/guest/booking/list", "/guest/booking/start", "/guest/review").authenticated()
                .requestMatchers("/community/write").authenticated()
                .requestMatchers("/host/room/**").hasAuthority(AuthorityCode.HOST.name())
                .anyRequest().permitAll()

            )

            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)

            )

            .formLogin(formLogin -> formLogin
                .usernameParameter("login-email")
                .passwordParameter("login-password")
                .loginPage("/user/login-page")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )

            .logout((logoutConfig) -> logoutConfig
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login-page")
                .invalidateHttpSession(true)
            )
            .userDetailsService(detailService);
        //                            //.requestMatchers("/**").hasAnyRole(AuthorityCode.SUPER_ADMIN.name())
        return http.build();
    }
}
