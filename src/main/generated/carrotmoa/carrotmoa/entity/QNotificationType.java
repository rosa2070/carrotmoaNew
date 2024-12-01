package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotificationType is a Querydsl query type for NotificationType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationType extends EntityPathBase<NotificationType> {

    private static final long serialVersionUID = -402052756L;

    public static final QNotificationType notificationType = new QNotificationType("notificationType");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QNotificationType(String variable) {
        super(NotificationType.class, forVariable(variable));
    }

    public QNotificationType(Path<? extends NotificationType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotificationType(PathMetadata metadata) {
        super(NotificationType.class, metadata);
    }

}

