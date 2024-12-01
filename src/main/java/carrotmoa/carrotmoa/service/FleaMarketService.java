package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.entity.PostImage;
import carrotmoa.carrotmoa.entity.Product;
import carrotmoa.carrotmoa.entity.ProductCategory;
import carrotmoa.carrotmoa.model.request.SaveFleaMarketPostRequest;
import carrotmoa.carrotmoa.model.request.SavePostImageRequest;
import carrotmoa.carrotmoa.model.request.UpdateFleaMarketPostRequest;
import carrotmoa.carrotmoa.model.request.UpdatePostRequest;
import carrotmoa.carrotmoa.model.response.FleaMarketPostDetailResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostImageResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostProductResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostUserResponse;
import carrotmoa.carrotmoa.model.response.ProductCategoryResponse;
import carrotmoa.carrotmoa.repository.FleaMarketPostRepository;
import carrotmoa.carrotmoa.repository.PostImageRepository;
import carrotmoa.carrotmoa.repository.PostRepository;
import carrotmoa.carrotmoa.repository.ProductCategoryRepository;
import carrotmoa.carrotmoa.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FleaMarketService {

    private final PostRepository postRepository;
    private final ProductRepository productRepository;
    private final PostImageRepository postImageRepository;
    private final FleaMarketPostRepository fleaMarketPostRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public Long savePost(SaveFleaMarketPostRequest saveFleaMarketPostRequest) {
        validatePost(saveFleaMarketPostRequest);
        Post post = postRepository.save(saveFleaMarketPostRequest.toPostEntity());
        productRepository.save(saveFleaMarketPostRequest.toProductEntity(post.getId()));

        List<String> imageUrls = extractImageUrls(saveFleaMarketPostRequest.getContent());
        if (!imageUrls.isEmpty()) {
            savePostImages(post.getId(), imageUrls);
        }

        return post.getId();
    }

    private void validatePost(SaveFleaMarketPostRequest saveFleaMarketPostRequest) {
        if (saveFleaMarketPostRequest.getTitle() == null || saveFleaMarketPostRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 사항입니다.");
        }
        if (saveFleaMarketPostRequest.getProductCategoryId() == null) {
            throw new IllegalArgumentException("카테고리는 필수 입력 사항입니다.");
        }
        if (saveFleaMarketPostRequest.getPrice() != null && saveFleaMarketPostRequest.getPrice() < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public Slice<FleaMarketPostResponse> getPostList(Pageable pageable) {
        return fleaMarketPostRepository.findPostList(pageable);
    }

    @Transactional(readOnly = true)
    public FleaMarketPostDetailResponse getPost(Long id) {
        FleaMarketPostProductResponse postProduct = fleaMarketPostRepository.findPostProductById(id);
        FleaMarketPostUserResponse postUser = fleaMarketPostRepository.findPostUserById(postProduct.getUserId());

        return FleaMarketPostDetailResponse.toFleaMarketPostDetailResponse(postProduct, postUser);
    }

    @Transactional
    public Long deletePost(Long id) {
        postRepository.markAsDeleted(id);
        return id;
    }

    @Transactional
    public Long updatePost(UpdatePostRequest updatePostRequest) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getCategoryList() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        return productCategoryList.stream()
            .map(ProductCategoryResponse::toProductCategoryResponse)
            .collect(Collectors.toList());
    }

    private List<String> extractImageUrls(String content) {
        if (content == null) {
            return new ArrayList<>();
        }
        List<String> imageUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            imageUrls.add(matcher.group(1));
        }
        return imageUrls;
    }

    private void savePostImages(Long postIdList, List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            SavePostImageRequest savePostImageRequest = new SavePostImageRequest(postIdList, imageUrl);
            postImageRepository.save(savePostImageRequest.toPostImageEntity());
        }
    }

    @Transactional(readOnly = true)
    public List<FleaMarketPostImageResponse> getPostImages(Long id) {

        List<PostImage> postImages = postImageRepository.findByPostId(id);
        return postImages.stream()
            .map(FleaMarketPostImageResponse::toFleaMarketPostImageResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long updatePost(UpdateFleaMarketPostRequest updateFleaMarketPostRequest, Long id) {
        Post post = postRepository.findOneById(id);
        Product product = productRepository.findOneByPostId(id);

        if (product == null) {
            throw new IllegalArgumentException("해당 제품이 존재하지 않습니다.");
        }

        post.setTitle(updateFleaMarketPostRequest.getTitle());
        post.setContent(updateFleaMarketPostRequest.getContent());

        // 가격을 업데이트 할 때, 가격이 null이 아니고 0 이상의 값인지 확인
        if (updateFleaMarketPostRequest.getPrice() != null && updateFleaMarketPostRequest.getPrice() >= 0) {
            product.setPrice(updateFleaMarketPostRequest.getPrice());
        } else {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        product.setProductCategoryId(updateFleaMarketPostRequest.getProductCategoryId());

        postRepository.save(post);
        productRepository.save(product);

        return id;
    }


    @Transactional(readOnly = true)
    public Slice<FleaMarketPostResponse> getPostListByUserId(Pageable pageable, Long userId) {
        return fleaMarketPostRepository.findByUserId(pageable, userId);
    }
}
