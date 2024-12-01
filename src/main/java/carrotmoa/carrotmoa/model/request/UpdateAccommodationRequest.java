package carrotmoa.carrotmoa.model.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Slf4j
public class UpdateAccommodationRequest {
    private Long userId;
    private String title;
    private Integer totalArea;
    private String roadAddress;
    private String lotAddress;
    private String detailAddress;
    private Integer floor;
    private Integer totalFloor;
    private BigDecimal price;
    private String content;
    private String transportationInfo;
    private BigDecimal latitude;
    private BigDecimal longitude;

    // 관계 테이블 리스트 (Space 테이블에 방, 화장실, 거실, 주방 순서로 담겨 있어야)
    private List<AccommodationSpaceRequest> accommodationSpaces = new ArrayList<>(); // 초기화
    private List<Long> amenityIds = new ArrayList<>();
    private List<MultipartFile> images; // 업로드된 이미지 파일들

}
