package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityCategory is a Querydsl query type for CommunityCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCategory extends EntityPathBase<CommunityCategory> {

    private static final long serialVersionUID = 330474048L;

    public static final QCommunityCategory communityCategory = new QCommunityCategory("communityCategory");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommunityCategory(String variable) {
        super(CommunityCategory.class, forVariable(variable));
    }

    public QCommunityCategory(Path<? extends CommunityCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityCategory(PathMetadata metadata) {
        super(CommunityCategory.class, metadata);
    }

}

