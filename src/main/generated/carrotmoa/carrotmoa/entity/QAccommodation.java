package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccommodation is a Querydsl query type for Accommodation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccommodation extends EntityPathBase<Accommodation> {

    private static final long serialVersionUID = 783863563L;

    public static final QAccommodation accommodation = new QAccommodation("accommodation");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detailAddress = createString("detailAddress");

    public final NumberPath<Integer> floor = createNumber("floor", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath lotAddress = createString("lotAddress");

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final StringPath roadAddress = createString("roadAddress");

    public final NumberPath<Integer> totalArea = createNumber("totalArea", Integer.class);

    public final NumberPath<Integer> totalFloor = createNumber("totalFloor", Integer.class);

    public final StringPath transportationInfo = createString("transportationInfo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAccommodation(String variable) {
        super(Accommodation.class, forVariable(variable));
    }

    public QAccommodation(Path<? extends Accommodation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccommodation(PathMetadata metadata) {
        super(Accommodation.class, metadata);
    }

}

