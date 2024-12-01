package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.CommunityCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {
    List<CommunityCategory> findByParentIdIsNotNull();

}
