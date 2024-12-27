package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.BestAccommodationResponse;
import carrotmoa.carrotmoa.repository.BestAccommodationCustomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BestAccommodationService {

    private final BestAccommodationCustomRepository bestAccommodationCustomRepository;

    public BestAccommodationService(BestAccommodationCustomRepository bestAccommodationCustomRepository) {
        this.bestAccommodationCustomRepository = bestAccommodationCustomRepository;

    }

    // RDS에서 이미 정렬된 인기 숙소 8개를 가져오는 메서드
    public List<BestAccommodationResponse> getBestAccommodationsFromRds() {
        log.info("Fetching best accommodations from RDS.");
        return bestAccommodationCustomRepository.getBestAccommodations();
    }

    // Redis에서 인기 숙소 8개를 가져오는 메서드
    @Cacheable(value="top_accommodations", key = "'top:8'", cacheManager = "bestAccommodationCacheManager")
    public List<BestAccommodationResponse> getBestAccommodationsFromRedis() {
        log.info("Fetching best accommodations from Redis list.");

        // Redis에서 데이터가 없으면 RDS에서 가져와서 Redis에 저장 (Cacheable이 이를 처리합니다)
        return getBestAccommodationsFromRds();
    }
}
