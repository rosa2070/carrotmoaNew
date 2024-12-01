package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.model.response.FleaMarketPostProductResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.model.response.FleaMarketPostUserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface FleaMarketPostCustomRepository {
    Slice<FleaMarketPostResponse> findPostList(Pageable pageable);

    FleaMarketPostProductResponse findPostProductById(Long id);

    FleaMarketPostUserResponse findPostUserById(Long id);

    Slice<FleaMarketPostResponse> findByKeyword(String keyword, Pageable pageable);

    Slice<FleaMarketPostResponse> findByUserId(Pageable pageable, Long id);
}