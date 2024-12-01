package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

}
