package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityPost is a Querydsl query type for CommunityPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityPost extends EntityPathBase<CommunityPost> {

    private static final long serialVersionUID = 966618850L;

    public static final QCommunityPost communityPost = new QCommunityPost("communityPost");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> communityCategoryId = createNumber("communityCategoryId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommunityPost(String variable) {
        super(CommunityPost.class, forVariable(variable));
    }

    public QCommunityPost(Path<? extends CommunityPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityPost(PathMetadata metadata) {
        super(CommunityPost.class, metadata);
    }

}

