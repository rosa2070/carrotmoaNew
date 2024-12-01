package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.SaveCommunityPostRequest;
import carrotmoa.carrotmoa.model.request.UpdateCommunityPostRequest;
import carrotmoa.carrotmoa.model.response.CommunityCategoryResponses;
import carrotmoa.carrotmoa.model.response.CommunityPostDetailResponse;
import carrotmoa.carrotmoa.model.response.CommunityPostListResponse;
import carrotmoa.carrotmoa.service.CommunityCategoryService;
import carrotmoa.carrotmoa.service.CommunityPostService;
import carrotmoa.carrotmoa.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@Slf4j
public class CommunityController {

    private final CommunityPostService communityPostService;
    private final CommunityCategoryService categoriesService;
    private final PostLikeService postLikeService;

    @GetMapping("/posts")
    public ResponseEntity<Slice<CommunityPostListResponse>> getAllCommunityPosts(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {
        Slice<CommunityPostListResponse> allCommunityPosts = communityPostService.getAllCommunityPosts(page, size);
        return new ResponseEntity<>(allCommunityPosts, HttpStatus.OK);
    }

    @GetMapping("/posts/subCategories/{subcategoryId}")
    public ResponseEntity<Slice<CommunityPostListResponse>> getPostsBySubCategory(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @PathVariable(name = "subcategoryId") Long subcategoryId) {
        Slice<CommunityPostListResponse> postsBySubCategory = communityPostService.getPostsBySubCategory(subcategoryId, page, size);
        return new ResponseEntity<>(postsBySubCategory, HttpStatus.OK);
    }


    @PostMapping("/posts")
    public ResponseEntity<Long> createCommunityPost(@RequestBody SaveCommunityPostRequest saveCommunityPostRequest) {
        log.info("게시글 삽입 데이터 확인: {}", saveCommunityPostRequest);
        Long postId = communityPostService.createCommunityPost(saveCommunityPostRequest);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    @GetMapping("/sub-categories")
    public ResponseEntity<CommunityCategoryResponses> getSubCategories() {
        CommunityCategoryResponses subCategories = categoriesService.getSubCategories();
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<CommunityCategoryResponses> getAllCategories() {
        CommunityCategoryResponses categories = categoriesService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/posts/{communityPostId}")
    public ResponseEntity<CommunityPostDetailResponse> findCommunityPostByPostId(@PathVariable("communityPostId") Long id,
        HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        CommunityPostDetailResponse communityPostByPostId = communityPostService.findCommunityPostDetail(id);
        return new ResponseEntity<>(communityPostByPostId, HttpStatus.OK);
    }

    @PutMapping("/posts/{communityPostId}")
    public ResponseEntity<Long> updateCommunityPost(@PathVariable("communityPostId") Long communityPostId,
        @RequestBody UpdateCommunityPostRequest request) {
        Long updateCommunityPostId = communityPostService.updateCommunityPost(communityPostId, request);
        return new ResponseEntity<>(updateCommunityPostId, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{communityPostId}")
    public ResponseEntity<Integer> deleteByCommunityPostId(@PathVariable("communityPostId") Long communityPostId) {
        int deletedCount = communityPostService.deleteByCommunityPostId(communityPostId);
        return new ResponseEntity<>(deletedCount, HttpStatus.OK);
    }

    // 게시글 좋아요 누르기 토글
    @PostMapping("/posts/{postId}/like/{userId}")
    public ResponseEntity<Boolean> toggleLike(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        boolean isLiked = postLikeService.toggleLike(postId, userId);
        return ResponseEntity.ok(isLiked);
    }

    // 해당 게시글의 좋아요 개수
    @Transactional(readOnly = true)
    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<Integer> getLikeCount(@PathVariable("postId") Long postId) {
        int likeCount = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }

    // 로그인한 유저가 게시글의 좋아요를 눌렀는지?
    @Transactional(readOnly = true)
    @GetMapping("/posts/{postId}/likes/{userId}")
    public ResponseEntity<Boolean> getLikeStatus(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        var isCanceledByPostIdAndUserId = postLikeService.findIsCanceledByPostIdAndUserId(postId, userId);
        return ResponseEntity.ok(isCanceledByPostIdAndUserId);
    }

}
