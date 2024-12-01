package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccommodationSpace is a Querydsl query type for AccommodationSpace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationSpace extends EntityPathBase<AccommodationSpace> {

    private static final long serialVersionUID = -1761877829L;

    public static final QAccommodationSpace accommodationSpace = new QAccommodationSpace("accommodationSpace");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> accommodationId = createNumber("accommodationId", Long.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> spaceId = createNumber("spaceId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodationSpace(String variable) {
        super(AccommodationSpace.class, forVariable(variable));
    }

    public QAccommodationSpace(Path<? extends AccommodationSpace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodationSpace(PathMetadata metadata) {
        super(AccommodationSpace.class, metadata);
    }

}

