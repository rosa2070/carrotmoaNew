package carrotmoa.carrotmoa.config;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAttribute {
    @ModelAttribute("user")
    public CustomUserDetails user(@AuthenticationPrincipal CustomUserDetails user) {return  user;}
}
