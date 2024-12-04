package carrotmoa.carrotmoa.controller;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sample")
@Controller
public class SampleViewController {

    @GetMapping("/user")
    public String userSample(@AuthenticationPrincipal CustomUserDetails user){
        //이메일
        System.out.println(user.getUsername());
        //UserLoginResponseDto
        System.out.println(user.getUserProfile());
        System.out.println(user.getUserProfile().getName());
        System.out.println(user.getUserProfile().getNickname());
        System.out.println(user.getUserProfile().getBio());
        System.out.println(user.getUserProfile().getBirthday());
        System.out.println(user.getUserProfile().getPicUrl());
        System.out.println(user.getUserProfile().getPhoneNumber());
        //UserAddressResponse
        System.out.println(user.getUserAddress().getAddressId());
        System.out.println(user.getUserAddress().getAddressName());
        System.out.println(user.getUserAddress().getRegion1DepthName());
        System.out.println(user.getUserAddress().getRegion2DepthName());
        System.out.println(user.getUserAddress().getRegion4DepthName());
        //유저 권한 (USER , HOST , ADMIN.....)
        System.out.println(user.getUserAuthority());
        return "user/use-sample";}

}
