package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.User;
import carrotmoa.carrotmoa.entity.UserAddress;
import carrotmoa.carrotmoa.entity.UserProfile;
import carrotmoa.carrotmoa.enums.AuthorityCode;
import carrotmoa.carrotmoa.model.request.UserAddressUpdateRequest;
import carrotmoa.carrotmoa.model.request.UserJoinDto;
import carrotmoa.carrotmoa.model.request.UserUpdateRequest;
import carrotmoa.carrotmoa.model.response.FindUserResponse;
import carrotmoa.carrotmoa.repository.AccountRepository;
import carrotmoa.carrotmoa.repository.UserAddressRepository;
import carrotmoa.carrotmoa.repository.UserProfileRepository;
import carrotmoa.carrotmoa.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode";
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserAddressRepository userAddressRepository;

    //이미 존재하는지 체크
    public boolean emailCheck(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)).isPresent();
    }

    public boolean nicknameCheck(String nickname) {
        return Optional.ofNullable(userProfileRepository.findByNickname(nickname)).isPresent();
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        CompletableFuture.runAsync(() -> {
            try {
                String title = "carrotmoa 인증번호";
                String authCode = this.createCode();
                mailService.sendEmail(toEmail, title, authCode);
                redisService.setData(AUTH_CODE_PREFIX + toEmail, authCode);
            } catch (MailSendException e) {
                // 예외 처리 로직 추가 (로그 기록 등)
                System.out.println("이메일 발송 실패: " + e.getMessage());
            }
        });
    }

    private void checkDuplicatedEmail(String email) {
        Optional.ofNullable(userRepository.findByEmail(email))
            .ifPresentOrElse(User ->
            {
                log.info("MemberService.checkDuplicatedEmail exception occur email: {}", email);
                throw new RuntimeException("checkDuplicationEmail exception");
            }, () -> {
                log.info("MemberService.checkDuplicatedEmail FALSE occur email: {}", email);
            });
    }

    //랜덤 값 생성
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= length; i++) {
                switch (random.nextInt(3)) {
                    case 0:
                        builder.append(random.nextInt(10));
                        break;
                    case 1:
                        builder.append((char) ('a' + random.nextInt(26)));
                        break;
                    case 2:
                        builder.append((char) ('A' + random.nextInt(26)));
                        break;
                }
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authCodeCertified(String email, String inputAuthCode) {
        return redisService.getData(email).equals(inputAuthCode);
    }

    @Transactional
    public boolean userJoin(UserJoinDto userJoinDto) {
        try {
            userJoinDto.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
            if (userJoinDto.getAuthorityId() != null && userJoinDto.getAuthorityId() == AuthorityCode.HOST.getId()) {
                userJoinDto.setAuthorityId(AuthorityCode.HOST.getId());
            } else {
                userJoinDto.setAuthorityId(AuthorityCode.USER.getId());
            }

            User saveUser = userRepository.save(userJoinDto.toUserEntity());
            userProfileRepository.save(userJoinDto.toUserProfileEntity(saveUser.getId()));

            if (saveUser.getAuthorityId() == AuthorityCode.HOST.getId()) {
                accountRepository.save(userJoinDto.toHostAdditionalFormEntity(saveUser.getId()));
            }
            return true;
        } catch (Exception e) {
            System.out.println("JoinException");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean userProfileUpdate(UserUpdateRequest request) {
        try {
            request.updateUser(
                userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지않음")),
                userProfileRepository.findByUserId(request.getUserId()));
            return true;
        } catch (Exception updateException) {
            System.out.println("UserUpdateException");
            System.out.println(updateException.getMessage());
            return false;
        }
    }
    @Transactional
    public boolean userAddressUpdate(UserAddressUpdateRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(request.getUserId());
        UserAddress entity = userAddressRepository.findByUserId(profile.getUserId());
        if (entity == null) {
            UserAddress userAddress = userAddressRepository.save(request.toEntityUserAddress());
            userProfileRepository.findByUserId(request.getUserId()).setAddressId(userAddress.getId());
            return true;
        } else {
            request.UserAddressUpdate(entity);

            return true;
        }
    }
    public FindUserResponse findUserNickname(String searchType, String searchKeyword) throws EntityNotFoundException{
        UserProfile profile = null;
        if (searchType.equals("userId")) {
            profile = userProfileRepository.findByUserId(Integer.parseInt(searchKeyword));
        } else if (searchType.equals("nickname")) {
            profile = userProfileRepository.findByNickname(searchKeyword);
        }
        if (profile == null) {
            throw new EntityNotFoundException();
        }
        return new FindUserResponse(profile);
    }


}
