package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.exception.ExternalApiException;
import carrotmoa.carrotmoa.exception.IgnoreException;
import carrotmoa.carrotmoa.model.response.BestAccommodationResponse;
import carrotmoa.carrotmoa.repository.BestAccommodationCustomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

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
    @CircuitBreaker(name = "simpleCircuitBreakerConfig", fallbackMethod = "fallback")
    public List<BestAccommodationResponse> getBestAccommodationsFromRedis() throws TimeoutException {
        log.info("✅ Redis 정상 동작 중...");

        // 예시로 Redis의 연결이 실패하도록 강제로 예외를 발생시킴
//        if (isRedisOverloaded()) {  // Redis가 과부하 상태라고 판단되는 조건
//            throw new RedisConnectionFailureException("Redis 서버 연결 실패 - 의도적인 예외 발생");
//        }

        return getBestAccommodationsFromRds();  // 정상적으로 데이터 가져오기
    }

    private boolean isRedisOverloaded() {
        // 예시로 트래픽이 일정 수치를 넘으면 Redis 과부하 상태로 간주하고 예외를 발생시킴
        // 실제 구현에서는 Redis 서버의 상태를 체크할 수 있는 로직을 추가해야 합니다.
        return true;  // 여기서는 예시로 항상 true 반환, 실제로는 조건에 맞게 설정
    }

    public List<BestAccommodationResponse> fallback(Throwable throwable) {
        log.error("❌ Redis 조회 실패, Fallback 호출: {}", throwable.getMessage());
        return getBestAccommodationsFromRds();
    }
}
