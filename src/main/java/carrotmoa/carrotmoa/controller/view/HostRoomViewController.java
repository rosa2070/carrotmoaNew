package carrotmoa.carrotmoa.controller.view;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/host/room")
@Slf4j
public class HostRoomViewController {

    // 방 등록
    @GetMapping("/register")
    public String showRegisterForm(Model model, @ModelAttribute("user") CustomUserDetails user) {

        // 로그인한 유저
        model.addAttribute("user", user);
        return "host/room_register";
    }

    // 방 관리 이동
    @GetMapping("/manage")
    public String getRoomManage(@ModelAttribute("user") CustomUserDetails user, Model model) {

        model.addAttribute("user", user);
        return "host/room_manage";
    }

    // 방 수정
    @GetMapping("/edit/{accommodationId}")
    public String showEditRoomForm(@PathVariable("accommodationId") Long accommodationId) {
        return "host/room_edit";
    }

    // 정산
    @GetMapping("/settlement")
    public String getSettlements(@ModelAttribute("user") CustomUserDetails user, Model model) {

        model.addAttribute("user", user);
        return "host/room_settlement";
    }

    // 계약
    @GetMapping("/contract")
    public String getContracts(@ModelAttribute("user") CustomUserDetails user, Model model) {

        model.addAttribute("user", user);
        return "host/room_contract";
    }
}
