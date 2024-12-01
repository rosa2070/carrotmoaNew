package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityComment is a Querydsl query type for CommunityComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityComment extends EntityPathBase<CommunityComment> {

    private static final long serialVersionUID = 128145629L;

    public static final QCommunityComment communityComment = new QCommunityComment("communityComment");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> communityPostId = createNumber("communityPostId", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> orderInGroup = createNumber("orderInGroup", Integer.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCommunityComment(String variable) {
        super(CommunityComment.class, forVariable(variable));
    }

    public QCommunityComment(Path<? extends CommunityComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityComment(PathMetadata metadata) {
        super(CommunityComment.class, metadata);
    }

}

