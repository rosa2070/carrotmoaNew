package carrotmoa.carrotmoa.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatRoomUser is a Querydsl query type for ChatRoomUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomUser extends EntityPathBase<ChatRoomUser> {

    private static final long serialVersionUID = -1242001179L;

    public static final QChatRoomUser chatRoomUser = new QChatRoomUser("chatRoomUser");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Long> chatRoomId = createNumber("chatRoomId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QChatRoomUser(String variable) {
        super(ChatRoomUser.class, forVariable(variable));
    }

    public QChatRoomUser(Path<? extends ChatRoomUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoomUser(PathMetadata metadata) {
        super(ChatRoomUser.class, metadata);
    }

}

