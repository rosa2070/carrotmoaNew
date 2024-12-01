package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(long userId);

    UserProfile findByNickname(String nickname);

    UserProfile findNicknameByUserId(long userId);

}
