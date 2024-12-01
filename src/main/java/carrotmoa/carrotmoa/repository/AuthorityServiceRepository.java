package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.AuthorityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityServiceRepository extends JpaRepository<AuthorityService, Long> {

}
