package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorityService is a Querydsl query type for AuthorityService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorityService extends EntityPathBase<AuthorityService> {

    private static final long serialVersionUID = 1194082457L;

    public static final QAuthorityService authorityService = new QAuthorityService("authorityService");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> authorityId = createNumber("authorityId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> serviceId = createNumber("serviceId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAuthorityService(String variable) {
        super(AuthorityService.class, forVariable(variable));
    }

    public QAuthorityService(Path<? extends AuthorityService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorityService(PathMetadata metadata) {
        super(AuthorityService.class, metadata);
    }

}

