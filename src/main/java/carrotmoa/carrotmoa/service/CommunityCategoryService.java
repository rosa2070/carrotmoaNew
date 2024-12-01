package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.CommunityCategory;
import carrotmoa.carrotmoa.model.response.CommunityCategoryResponse;
import carrotmoa.carrotmoa.model.response.CommunityCategoryResponses;
import carrotmoa.carrotmoa.repository.CommunityCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityCategoryService {

    private final CommunityCategoryRepository categoriesRepository;

    /**
     * 공통 로직: 카테고리 조회 및 응답 변환
     */
    private CommunityCategoryResponses getCategoriesResponse(List<CommunityCategory> categoriesEntity) {
        log.info("카테고리 조회 완료, 카테고리 수: {}", categoriesEntity.size());
        List<CommunityCategoryResponse> responseList = categoriesEntity.stream()
                .map(CommunityCategoryResponse::new)
                .toList();
        return new CommunityCategoryResponses(responseList);
    }

    @Cacheable(value = "communityCategories", key = "'subCategories'", cacheManager = "communityCacheManager")
    @Transactional(readOnly = true)
    public CommunityCategoryResponses getSubCategories() {
        log.info("getSubCategories: DB에서 값을 조회하고 있음, 캐시 사용 X");
        List<CommunityCategory> categoriesEntity = categoriesRepository.findByParentIdIsNotNull();
        return getCategoriesResponse(categoriesEntity); // 공통 로직 호출
    }

    @Cacheable(value = "communityCategories", key = "'allCategories'", cacheManager = "communityCacheManager")
    @Transactional(readOnly = true)
    public CommunityCategoryResponses getAllCategories() {
        log.info("getAllCategories: DB에서 값을 조회하고 있음, 캐시 사용 X");
        List<CommunityCategory> categoriesEntity = categoriesRepository.findAll();
        return getCategoriesResponse(categoriesEntity); // 공통 로직 호출
    }

}