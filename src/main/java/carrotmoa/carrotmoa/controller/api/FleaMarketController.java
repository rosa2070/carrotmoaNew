package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.SaveFleaMarketPostRequest;
import carrotmoa.carrotmoa.model.request.UpdateFleaMarketPostRequest;
import carrotmoa.carrotmoa.model.request.UpdatePostRequest;
import carrotmoa.carrotmoa.model.response.FleaMarketPostDetailResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostImageResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.model.response.ProductCategoryResponse;
import carrotmoa.carrotmoa.service.FleaMarketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "중고거래", description = "중고거래 페이지")
@RequestMapping("/api/fleamarket")
@RestController
@RequiredArgsConstructor
public class FleaMarketController {

    private final FleaMarketService fleaMarketService;

    @PostMapping("/save")
    public Long createPost(@RequestBody SaveFleaMarketPostRequest saveFleaMarketPostRequest) {
        return fleaMarketService.savePost(saveFleaMarketPostRequest);
    }

    @GetMapping("/list")
    public Slice<FleaMarketPostResponse> getPostList(@PageableDefault(size = 9) Pageable pageable) {
        return fleaMarketService.getPostList(pageable);
    }

    @GetMapping("/other-product")
    public Slice<FleaMarketPostResponse> getPostListByUserId(@PageableDefault(size = 3) Pageable pageable, Long userId) {
        return fleaMarketService.getPostListByUserId(pageable, userId);
    }

    @GetMapping("/posts/{id}")
    public FleaMarketPostDetailResponse getPost(@PathVariable("id") Long id) {
        return fleaMarketService.getPost(id);
    }

    @GetMapping("/category")
    public List<ProductCategoryResponse> getCategoryList() {
        return fleaMarketService.getCategoryList();
    }

    @DeleteMapping("/posts/{postId}")
    public Long deletePost(@PathVariable("postId") Long postId) {
        return fleaMarketService.deletePost(postId);
    }

    @PutMapping("/posts/{id}")
    public Long updatePost(@RequestBody UpdateFleaMarketPostRequest updateFleaMarketPostRequest, @PathVariable("id") Long id) {
        return fleaMarketService.updatePost(updateFleaMarketPostRequest,id);
    }

    @GetMapping("/images/{id}")
    public List<FleaMarketPostImageResponse> getPostImage(@PathVariable("id") Long id) {
        return fleaMarketService.getPostImages(id);
    }


}
