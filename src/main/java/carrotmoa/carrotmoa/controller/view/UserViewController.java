package carrotmoa.carrotmoa.controller.view;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.model.request.ChatMessageRequest;
import carrotmoa.carrotmoa.model.request.UserJoinDto;
import carrotmoa.carrotmoa.model.request.UserUpdateRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserViewController {

    @GetMapping("/login-page")
    public String userLoginForm() {
        return "user/login-page";}

    @GetMapping("/join-form")
    public String userJoinForm(Model model) {
        model.addAttribute("userJoinDto", new UserJoinDto());
        return "user/join";}

    @GetMapping("/my-page")
    public String userMyPage(Model model) {
        model.addAttribute("fragment","main");
        return "/user/my-page";}

    @GetMapping("/profile-update")
    public String userUpdateProfileForm(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("userUpdateRequestDto", new UserUpdateRequest(user));
        model.addAttribute("fragment", "profile-update");
        return "/user/my-page";
    }

    @GetMapping("/password-update")
    public String userPasswordUpdateForm(Model model) {
        model.addAttribute("fragment","profile-password-update");
        return "/user/my-page";}

    @GetMapping("/my-page-location")
    public String userLocationUpdate(Model model) {
        model.addAttribute("fragment","my-page-location");
        return "/user/my-page";}

    @GetMapping("/my-page-service1")
    public String myPageService1(Model model) {
        model.addAttribute("fragment", "service1");
        return "/user/my-page";
    }

    @GetMapping("/my-page-service2")
    public String myPageService2(Model model) {
        model.addAttribute("fragment", "service2");
        return "/user/my-page";
    }

    @GetMapping("/my-page-service3")
    public String myPageService3(Model model) {
        model.addAttribute("fragment", "service3");
        return "/user/my-page";
    }

    @GetMapping("/my-page-service4")
    public String myPageService4(Model model) {
        model.addAttribute("fragment", "service4");
        return "/user/my-page";
    }

    @GetMapping("/modal-content")
    public String chatTest(@AuthenticationPrincipal CustomUserDetails user,Model model){
        model.addAttribute("chatMessageRequest",new ChatMessageRequest(user));
        return "/user/chat";}

}


