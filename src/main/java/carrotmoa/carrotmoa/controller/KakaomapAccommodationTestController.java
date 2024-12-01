package carrotmoa.carrotmoa.controller;

import carrotmoa.carrotmoa.testdto.ExhibitionDTO;
import carrotmoa.carrotmoa.testdto.PerformanceDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class KakaomapAccommodationTestController {

    private static final Random random = new Random();

    // 서울과 경기 내 랜덤 위도 생성
    private double getRandomLatitude() {
        double minLat = 37.0; // 서울 및 경기 남쪽 경계
        double maxLat = 37.8; // 서울 및 경기 북쪽 경계
        return minLat + (maxLat - minLat) * random.nextDouble();
    }

    // 서울과 경기 내 랜덤 경도 생성
    private double getRandomLongitude() {
        double minLon = 126.7; // 서울 및 경기 서쪽 경계
        double maxLon = 127.5; // 서울 및 경기 동쪽 경계
        return minLon + (maxLon - minLon) * random.nextDouble();
    }

    @GetMapping("/accommodation-data")
    public Map<String, Object> getTicketData() {
        // ExhibitionDTO 더미 데이터 1000개 생성
        List<ExhibitionDTO> exhibitions = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            exhibitions.add(new ExhibitionDTO(
                "전시회 " + i,
                "서울시 종로구 " + i,
                "테마 " + i,
                getRandomLatitude(), // 랜덤 위도 생성
                getRandomLongitude()  // 랜덤 경도 생성
            ));
        }

        // PerformanceDTO 더미 데이터 1000개 생성
        List<PerformanceDTO> performances = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            performances.add(new PerformanceDTO(
                "공연 " + i,
                "서울시 강남구 " + i,
                "2024-10-" + String.format("%02d", i), // 날짜 값 변경
                getRandomLatitude(), // 랜덤 위도 생성
                getRandomLongitude()  // 랜덤 경도 생성
            ));
        }

        // 두 DTO 리스트를 맵으로 묶어서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("exhibitions", exhibitions);
        response.put("performances", performances);

        return response;
    }
}
