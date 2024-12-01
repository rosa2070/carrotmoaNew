package carrotmoa.carrotmoa.repository;

import static carrotmoa.carrotmoa.entity.QPost.post;
import static carrotmoa.carrotmoa.entity.QProduct.product;
import static carrotmoa.carrotmoa.entity.QProductCategory.productCategory;
import static carrotmoa.carrotmoa.entity.QUserAddress.userAddress;
import static carrotmoa.carrotmoa.entity.QUserProfile.userProfile;

import carrotmoa.carrotmoa.enums.ProductStatus;
import carrotmoa.carrotmoa.model.response.FleaMarketPostProductResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostUserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class FleaMarketCustomRepositoryImpl implements FleaMarketPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public FleaMarketCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<FleaMarketPostResponse> findPostList(Pageable pageable) {
        List<FleaMarketPostResponse> posts = getPostList(pageable).fetch();
        boolean hasNext = hasNextPage(posts, pageable.getPageSize());
        return new SliceImpl<>(posts, pageable, hasNext);
    }

    private JPAQuery<FleaMarketPostResponse> getPostList(Pageable pageable) {
        return jpaQueryFactory
            .select(
                Projections.constructor(FleaMarketPostResponse.class,
                    post.id,
                    post.userId,
                    post.title,
                    post.content,
                    product.id,
                    product.price,
                    product.isPriceOffer,
                    product.isFree,
                    product.status,
                    userAddress.region1DepthName,
                    userAddress.region2DepthName,
                    userAddress.region3DepthName,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(product).on(post.id.eq(product.postId))
            .leftJoin(userAddress).on(post.userId.eq(userAddress.userId))
            .where(post.serviceId.eq(1L),
                product.postId.isNotNull(),
                post.isDeleted.isFalse(),
                product.status.ne(ProductStatus.SOLD_OUT))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1);
    }

    private boolean hasNextPage(List<FleaMarketPostResponse> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }

    @Override
    public FleaMarketPostProductResponse findPostProductById(Long id) {
        return jpaQueryFactory
            .select(
                Projections.constructor(FleaMarketPostProductResponse.class,
                    post.id,
                    post.userId,
                    productCategory.name,
                    post.title,
                    post.content,
                    product.price,
                    product.isPriceOffer,
                    product.isFree,
                    product.status,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(product).on(post.id.eq(product.postId))
            .leftJoin(productCategory).on(product.productCategoryId.eq(productCategory.id))
            .where(post.id.eq(id))
            .fetchOne();
    }

    @Override
    public FleaMarketPostUserResponse findPostUserById(Long userId) {
        return jpaQueryFactory
            .select(
                Projections.constructor(FleaMarketPostUserResponse.class,
                    userProfile.picUrl,
                    userProfile.nickname,
                    userAddress.region1DepthName,
                    userAddress.region2DepthName,
                    userAddress.region3DepthName
                )
            )
            .from(userProfile)
            .leftJoin(userAddress).on(userProfile.userId.eq(userAddress.userId))
            .where(userProfile.userId.eq(userId))
//            .where(userAddress.userId.eq(userId))
            .fetchOne();
    }

    @Override
    public Slice<FleaMarketPostResponse> findByKeyword(String keyword, Pageable pageable) {
        List<FleaMarketPostResponse> posts = getPostListByKeyword(keyword, pageable).fetch();
        boolean hasNext = hasNextPage(posts, pageable.getPageSize());
        return new SliceImpl<>(posts, pageable, hasNext);
    }

    private JPAQuery<FleaMarketPostResponse> getPostListByKeyword(String keyword, Pageable pageable) {
        return jpaQueryFactory
            .select(
                Projections.constructor(FleaMarketPostResponse.class,
                    post.id,
                    post.userId,
                    post.title,
                    post.content,
                    product.id,
                    product.price,
                    product.isPriceOffer,
                    product.isFree,
                    product.status,
                    userAddress.region1DepthName,
                    userAddress.region2DepthName,
                    userAddress.region3DepthName,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(product).on(post.id.eq(product.postId))
            .leftJoin(userAddress).on(post.userId.eq(userAddress.userId))
            .where(post.serviceId.eq(1L),
                product.postId.isNotNull(),
                post.isDeleted.isFalse(),
                product.status.ne(ProductStatus.SOLD_OUT),
                post.title.containsIgnoreCase(keyword))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1);
    }

    @Override
    public Slice<FleaMarketPostResponse> findByUserId(Pageable pageable, Long id) {
        List<FleaMarketPostResponse> posts = getPostListByUserId(id, pageable).fetch();
        boolean hasNext = hasNextPage(posts, pageable.getPageSize());
        return new SliceImpl<>(posts, pageable, hasNext);
    }

    private JPAQuery<FleaMarketPostResponse> getPostListByUserId(Long id, Pageable pageable) {
        return jpaQueryFactory
            .select(
                Projections.constructor(FleaMarketPostResponse.class,
                    post.id,
                    post.userId,
                    post.title,
                    post.content,
                    product.id,
                    product.price,
                    product.isPriceOffer,
                    product.isFree,
                    product.status,
                    userAddress.region1DepthName,
                    userAddress.region2DepthName,
                    userAddress.region3DepthName,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(product).on(post.id.eq(product.postId))
            .leftJoin(userAddress).on(post.userId.eq(userAddress.userId))
            .where(post.serviceId.eq(1L),
                product.postId.isNotNull(),
                post.isDeleted.isFalse(),
                product.status.ne(ProductStatus.SOLD_OUT),
                post.userId.eq(49L))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1);
    }
}