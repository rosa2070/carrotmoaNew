package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.QAccommodation;
import carrotmoa.carrotmoa.entity.QAccommodationAmenity;
import carrotmoa.carrotmoa.entity.QAccommodationImage;
import carrotmoa.carrotmoa.entity.QAccommodationSpace;
import carrotmoa.carrotmoa.entity.QPost;
import carrotmoa.carrotmoa.model.response.AccommodationDetailResponse;
import carrotmoa.carrotmoa.model.response.HostManagedAccommodationResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AccommodationDetailCustomRepositoryImpl implements AccommodationDetailCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QAccommodation accommodation = QAccommodation.accommodation;
    private final QAccommodationImage accommodationImage = QAccommodationImage.accommodationImage;
    private final QAccommodationAmenity accommodationAmenity = QAccommodationAmenity.accommodationAmenity;
    private final QAccommodationSpace accommodationSpace = QAccommodationSpace.accommodationSpace;
    private final QPost post = QPost.post;

    public AccommodationDetailCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public AccommodationDetailResponse getAccommodationDetailById(Long id) {
        AccommodationDetailResponse detailResponse = jpaQueryFactory
            .select(Projections.fields(AccommodationDetailResponse.class,
                accommodation.id,
                post.title,
                accommodation.totalArea,
                accommodation.roadAddress,
                accommodation.lotAddress,
                accommodation.detailAddress,
                accommodation.floor,
                accommodation.totalFloor,
                accommodation.price,
                post.content,
                accommodation.transportationInfo
            ))
            .from(accommodation)
            .leftJoin(post).on(accommodation.postId.eq(post.id))
            .where(accommodation.id.eq(id))
            .fetchOne();

        // 이미지 URL 쿼리
        List<String> imageUrls = jpaQueryFactory
            .select(accommodationImage.imageUrl)
            .from(accommodationImage)
            .where(accommodationImage.accommodationId.eq(id))
            .fetch();

        List<Long> amenityIds = jpaQueryFactory
            .select(accommodationAmenity.amenityId)
            .from(accommodationAmenity)
            .where(accommodationAmenity.accommodationId.eq(id))
            .fetch();

        List<Integer> spaceCounts = jpaQueryFactory
            .select(accommodationSpace.count)
            .from(accommodationSpace)
            .where(accommodationSpace.accommodationId.eq(id))
            .fetch();

        if (detailResponse != null) {
            detailResponse.setImageUrls(imageUrls);
            detailResponse.setAmenityIds(amenityIds);
            detailResponse.setSpaceCounts(spaceCounts);
        }

        return detailResponse;
    }

    @Override
    public List<HostManagedAccommodationResponse> findAccommodationsByUserId(Long userId, Long lastId, int limit) {
        // 첫 요청 시 lastId가 0이면, ID가 가장 큰 값을 기준으로 시작
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.userId.eq(userId))  // userId 필터링
            .and(post.isDeleted.eq(false)) // 삭제되지 않은 포스트 필터링
            .and(accommodationImage.imageOrder.eq(0)); // 대표 이미지 필터링

        // `lastId`가 0이 아니면 그 이후의 ID를 가져오는 조건 추가
        if (lastId > 0) {
            builder.and(accommodation.id.lt(lastId)); // 이전 데이터보다 작은 ID 기준으로
        }

        // 최신순 (내림차순) 정렬
        return jpaQueryFactory
            .select(Projections.fields(HostManagedAccommodationResponse.class,
                accommodation.id,
                post.title,
                accommodation.lotAddress,
                accommodation.detailAddress,
                accommodation.price,
                accommodationImage.imageUrl
            ))
            .from(accommodation)
            .join(post).on(accommodation.postId.eq(post.id))
            .join(accommodationImage).on(accommodationImage.accommodationId.eq(accommodation.id))
            .where(builder)
            .orderBy(accommodation.id.desc())  // 최신순 (id 내림차순)
            .limit(limit) // 최대 limit 개수의 데이터만 반환
            .fetch();
    }

    @Override
    public List<HostManagedAccommodationResponse> getAllHostRooms(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(HostManagedAccommodationResponse.class,
                accommodation.id,  // accommodation.id 가져오기
                post.title          // post.title 가져오기
            ))
            .from(accommodation)
            .join(post).on(accommodation.postId.eq(post.id))  // accommodation.postId = post.id
            .where(post.userId.eq(userId)
                .and(post.isDeleted.eq(false)))  // userId로 필터링// 삭제되지 않은 포스트만 필터링
            .orderBy(accommodation.id.desc())  // 최신 순으로 정렬
            .fetch();  // 결과를 List로 반환
    }


}
