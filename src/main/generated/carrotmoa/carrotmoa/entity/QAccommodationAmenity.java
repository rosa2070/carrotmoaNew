package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccommodationAmenity is a Querydsl query type for AccommodationAmenity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationAmenity extends EntityPathBase<AccommodationAmenity> {

    private static final long serialVersionUID = 175465774L;

    public static final QAccommodationAmenity accommodationAmenity = new QAccommodationAmenity("accommodationAmenity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> accommodationId = createNumber("accommodationId", Long.class);

    public final NumberPath<Long> amenityId = createNumber("amenityId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodationAmenity(String variable) {
        super(AccommodationAmenity.class, forVariable(variable));
    }

    public QAccommodationAmenity(Path<? extends AccommodationAmenity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodationAmenity(PathMetadata metadata) {
        super(AccommodationAmenity.class, metadata);
    }

}

