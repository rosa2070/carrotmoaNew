package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccommodationImage is a Querydsl query type for AccommodationImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodationImage extends EntityPathBase<AccommodationImage> {

    private static final long serialVersionUID = -1771202288L;

    public static final QAccommodationImage accommodationImage = new QAccommodationImage("accommodationImage");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> accommodationId = createNumber("accommodationId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> imageOrder = createNumber("imageOrder", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodationImage(String variable) {
        super(AccommodationImage.class, forVariable(variable));
    }

    public QAccommodationImage(Path<? extends AccommodationImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodationImage(PathMetadata metadata) {
        super(AccommodationImage.class, metadata);
    }

}

