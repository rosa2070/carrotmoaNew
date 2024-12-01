package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.UserAddress;
import carrotmoa.carrotmoa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    UserAddress findByUserId(long userId);
}
