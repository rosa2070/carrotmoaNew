package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHit is a Querydsl query type for Hit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHit extends EntityPathBase<Hit> {

    private static final long serialVersionUID = 2034713996L;

    public static final QHit hit = new QHit("hit");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QHit(String variable) {
        super(Hit.class, forVariable(variable));
    }

    public QHit(Path<? extends Hit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHit(PathMetadata metadata) {
        super(Hit.class, metadata);
    }

}

