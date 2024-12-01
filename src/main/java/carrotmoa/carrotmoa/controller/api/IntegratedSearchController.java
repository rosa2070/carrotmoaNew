package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.AccommodationSearchResponse;
import carrotmoa.carrotmoa.model.response.CommunityPostSearchResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integrated-search")
@RequiredArgsConstructor
public class IntegratedSearchController {
    private final PostService postService;

    // 1. 커뮤니티 게시글
    @GetMapping("/community")
    public ResponseEntity<Slice<CommunityPostSearchResponse>> communitySearch(
        @RequestParam(name = "keyword") String keyword,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "6") int size) {
        return ResponseEntity.ok(postService.integratedSearchCommunityPost(keyword, page, size));
    }

    @GetMapping("/fleamarket")
    public ResponseEntity<Slice<FleaMarketPostResponse>> fleMarketSearch(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        return ResponseEntity.ok(postService.integratedSearchFleaMarketPost(keyword, page, size));
    }

    //     3. 숙소정보 검색 api 제작
    @GetMapping("/accommodation")
    public ResponseEntity<Slice<AccommodationSearchResponse>> AccommodationSearch(
        @RequestParam(name = "keyword") String keyword,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "6") int size) {
        return ResponseEntity.ok(postService.integratedSearchAccommodationPost(keyword, page, size));
    }
}
