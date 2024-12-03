package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.AccommodationLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationLikeRepository extends JpaRepository<AccommodationLike, Long> {

    // 사용자와 숙소 ID를 기반으로 찜한 항목을 조회
    // select exists (select 1 from accommodation_like where user_id = ? and accommodatin_id = ?);
    boolean existsByUserIdAndAccommodationId(Long userId, Long accommodationId);

    // 사용자와 숙소 ID를 기반으로 찜한 항목을 삭제
    // delete from accommodation_like where user_id = ? and accommodation_id = ?
    void deleteByUserIdAndAccommodationId(Long userId, Long accommodationId);

}
