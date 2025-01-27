package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.db.RouteDataSource;
import carrotmoa.carrotmoa.entity.QAccommodation;
import carrotmoa.carrotmoa.entity.QAccommodationImage;
import carrotmoa.carrotmoa.entity.QPost;
import carrotmoa.carrotmoa.entity.QReservation;
import carrotmoa.carrotmoa.model.response.BestAccommodationResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static carrotmoa.carrotmoa.db.RouteDataSource.DataSourceType.SLAVE;

@Repository
@Transactional(readOnly = true)
//@RouteDataSource(dataSourceType = SLAVE)
public class BestAccommodationCustomRepositoryImpl implements BestAccommodationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QReservation reservation = QReservation.reservation;
    private final QAccommodation accommodation = QAccommodation.accommodation;
    private final QAccommodationImage accommodationImage = QAccommodationImage.accommodationImage;
    private final QPost post = QPost.post;

    public BestAccommodationCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<BestAccommodationResponse> getBestAccommodations() {
        return jpaQueryFactory
                .select(Projections.fields(BestAccommodationResponse.class,
                        accommodation.id,
                        post.title,
                        accommodation.lotAddress,
                        accommodation.price,
                        accommodationImage.imageUrl,
                        reservation.count().as("reservationCount")))  // reservation_count를 reservationCount로 매핑
                .from(reservation)
                .join(accommodation).on(reservation.accommodationId.eq(accommodation.id))
                .join(accommodationImage).on(accommodationImage.accommodationId.eq(accommodation.id))
                .join(post).on(post.id.eq(accommodation.postId))
                .where(reservation.status.eq(1).and(accommodationImage.imageOrder.eq(0)))
                .groupBy(reservation.accommodationId)
                .orderBy(reservation.count().desc())
                .limit(8)
                .fetch();
    }

    @Override
    public List<BestAccommodationResponse> getTopBestAccommodations() {
        return jpaQueryFactory
                .select(Projections.fields(BestAccommodationResponse.class,
                        accommodation.id,
                        reservation.count().as("reservationCount")))
                .from(reservation)
                .join(accommodation).on(reservation.accommodationId.eq(accommodation.id))
                .where(reservation.status.eq(1))
                .groupBy(accommodation.id)
                .orderBy(reservation.count().desc())
                .fetch();
    }


}
