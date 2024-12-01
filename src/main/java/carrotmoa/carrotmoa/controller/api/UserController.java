package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.UserAddressUpdateRequest;
import carrotmoa.carrotmoa.model.request.UserJoinDto;
import carrotmoa.carrotmoa.model.request.UserUpdateRequest;
import carrotmoa.carrotmoa.model.response.FindUserResponse;
import carrotmoa.carrotmoa.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/email-check")
    public ResponseEntity<Boolean> emailCheck(@RequestParam("email") String email) {
        return new ResponseEntity<Boolean>(userService.emailCheck(email), HttpStatus.OK);
    }

    @GetMapping("/auth-code-send")
    public ResponseEntity<Boolean> authCodeEmailSend(@RequestParam("email") String email) {
        boolean result;
        try {
            userService.sendCodeToEmail(email);
            result = true;
        } catch (MailSendException e) {
            result = false;
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @GetMapping("/auth-code-certified")
    public ResponseEntity<Boolean> authCodeCertified(@RequestParam("email") String email, @RequestParam("inputauthcode") String inputAuthCode) {
        return new ResponseEntity<Boolean>(userService.authCodeCertified(email, inputAuthCode), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<Boolean> userJoinSubmit(@RequestBody UserJoinDto userJoinDto) {
        return new ResponseEntity<Boolean>(userService.userJoin(userJoinDto), HttpStatus.OK);
    }

    @GetMapping("/nickname-duplication")
    public ResponseEntity<Boolean> nicknameDuplication(@RequestParam("nickname") String nickname) {
        return new ResponseEntity<Boolean>(userService.nicknameCheck(nickname), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> userProfileUpdate(@RequestBody UserUpdateRequest userUpdateRequestDto) {
        return new ResponseEntity<Boolean>(userService.userProfileUpdate(userUpdateRequestDto), HttpStatus.OK);
    }
    @PostMapping("/address-update")
    public ResponseEntity<Boolean> userAddressUpdate(@RequestBody UserAddressUpdateRequest request){
        return new ResponseEntity<>(userService.userAddressUpdate(request),HttpStatus.OK);
    }

    @GetMapping("/find-user/{searchType}/{searchKeyword}")
    public ResponseEntity<FindUserResponse> findUser(@PathVariable String searchType, @PathVariable String searchKeyword) {
        return new ResponseEntity<>(userService.findUserNickname(searchType, searchKeyword), HttpStatus.OK);
    }


}

