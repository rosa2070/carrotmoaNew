package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserId(Long UserId);
}
