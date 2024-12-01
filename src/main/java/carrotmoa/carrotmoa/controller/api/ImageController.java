package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.service.CommunityImageService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final CommunityImageService communityImageService;

    @PostMapping("/images/upload")
    public ResponseEntity<Map<String, Object>> imageUpload(MultipartRequest request) {
        Map<String, Object> responseData = new HashMap<>();

        try {
            // 이미지 업로드 후 S3 URL 반환
            String s3Url = communityImageService.imageUpload(request);
            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
            log.info("AWS S3 URL: {}", s3Url);

            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (IOException e) {
            responseData.put("uploaded", false);
            log.error("Image upload failed", e);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }
}
