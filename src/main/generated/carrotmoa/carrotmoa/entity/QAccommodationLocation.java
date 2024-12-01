package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccommodationLocation is a Querydsl query type for AccommodationLocation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationLocation extends EntityPathBase<AccommodationLocation> {

    private static final long serialVersionUID = 546609120L;

    public static final QAccommodationLocation accommodationLocation = new QAccommodationLocation("accommodationLocation");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> accommodationId = createNumber("accommodationId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodationLocation(String variable) {
        super(AccommodationLocation.class, forVariable(variable));
    }

    public QAccommodationLocation(Path<? extends AccommodationLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodationLocation(PathMetadata metadata) {
        super(AccommodationLocation.class, metadata);
    }

}

