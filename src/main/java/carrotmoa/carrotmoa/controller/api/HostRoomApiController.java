package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.model.response.AccommodationDetailResponse;
import carrotmoa.carrotmoa.model.response.HostManagedAccommodationResponse;
import carrotmoa.carrotmoa.service.AccommodationHostService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/host/room")
//@Validated
public class HostRoomApiController {
    // 기본 생성 공간 수 (방, 화장실, 거실, 주방)
    private static final int DEFAULT_SPACE_COUNT = 4;

    private final AccommodationHostService accommodationHostService;


    public HostRoomApiController(AccommodationHostService accommodationHostService) {
        this.accommodationHostService = accommodationHostService;

    }

    // 방 등록 폼 제출 후 테이블에 값 들어옴
    @PostMapping("/register")
    public ResponseEntity<?> registerAccommodation(
        @Valid @ModelAttribute SaveAccommodationRequest saveAccommodationRequest,
        BindingResult bindingResult) {

        // Service로 옮겨야?? ㄴㄴ service전 체크하는거라
        // 유효성 검사 실패 시 처리
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> {
                    String errorMessage = fieldError.getDefaultMessage();
                    // 오류 메시지를 로그로 기록
                    log.error("유효성 검사 오류 - 필드: {}, 메시지: {}", fieldError.getField(), errorMessage);
                    return errorMessage;
                })
                .collect(Collectors.toList());

            // 이미지 업로드 관련 오류 메시지 추가
            List<MultipartFile> images = saveAccommodationRequest.getImages();
            if (images != null) {
                int imageCount = images.size();
                if (imageCount < 4 || imageCount > 20) {
                    errorMessages.add("이미지는 최소 4개, 최대 20개까지 업로드할 수 있습니다.");
                }
            }

            // 오류 메시지를 포함한 응답 반환
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // 공간 초기화 메서드 호출
        if (saveAccommodationRequest.getAccommodationSpaces().isEmpty()) {
            saveAccommodationRequest.initializeSpaces(DEFAULT_SPACE_COUNT);
        }

        // 여기서 AOP가 자동으로 logBefore 메서드를 호출하여 로깅을 수행합니다.

        Long accommodationId = accommodationHostService.createAccommodation(saveAccommodationRequest);
        return new ResponseEntity<>(accommodationId, HttpStatus.CREATED);
    }

    // 숙소 정보 업데이트 (PATCH 요청)
    @PatchMapping("/edit/{id}")
    public ResponseEntity<String> updateAccommodation(
        @PathVariable("id") Long id,
        @ModelAttribute UpdateAccommodationRequest updateAccommodationRequest,
        @RequestParam(value = "existingImageUrls", required = false) List<String> existingImageUrls) {

        log.info("기존 이미지 URL: {}", existingImageUrls);
        log.info("업로드된 이미지 개수: {}", (updateAccommodationRequest.getImages() != null ? updateAccommodationRequest.getImages().size() : 0));

        // 숙소 정보 업데이트
        try {
            accommodationHostService.updateAccommodation(id, updateAccommodationRequest, existingImageUrls);
            return ResponseEntity.ok("숙소 정보가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 중 오류가 발생했습니다.");
        }
    }


    // 방 상세
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodationDetail(@PathVariable("id") Long id) {
        AccommodationDetailResponse accommodationDetailResponse = accommodationHostService.getAccommodationDetail(id);
        if (accommodationDetailResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accommodationDetailResponse);
    }

    // 호스트가 등록한 방 리스트 보이기(방 관리)
    @GetMapping("/manage/{userId}")
    public ResponseEntity<List<HostManagedAccommodationResponse>> getAccommodationsByUserId(
        @PathVariable("userId") Long userId,
        @RequestParam(value = "lastId", defaultValue = "0") Long lastId, // 초기 값은 0
        @RequestParam(value = "limit", defaultValue = "10") int limit) { // 한 번에 불러올 항목 수
        List<HostManagedAccommodationResponse> accommodations = accommodationHostService.getAccommodationsByUserId(userId, lastId, limit);

        if (accommodations == null || accommodations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 200 OK와 빈 배열 반환
        }

        return ResponseEntity.ok(accommodations);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<HostManagedAccommodationResponse>> getAllHostRooms(@PathVariable("userId") Long userId) {
        List<HostManagedAccommodationResponse> accommodations = accommodationHostService.getAllHostRooms(userId);

        if (accommodations == null || accommodations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 빈 리스트 반환
        }

        return ResponseEntity.ok(accommodations); // 200 OK와 함께 숙소 리스트 반환

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable("id") Long id) {

        try {
            accommodationHostService.deleteAccommodation(id);
            return ResponseEntity.ok("숙소가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("숙소 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
        }
    }





}
