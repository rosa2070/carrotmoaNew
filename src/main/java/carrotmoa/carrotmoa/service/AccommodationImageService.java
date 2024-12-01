package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.AccommodationImage;
import carrotmoa.carrotmoa.repository.AccommodationImageRepository;
import carrotmoa.carrotmoa.util.AwsS3Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class AccommodationImageService {
    private static final String IMAGE_FOLDER = "room";

    private final AccommodationImageRepository accommodationImageRepository;
    private final AwsS3Utils awsS3Utils;

    public AccommodationImageService(AccommodationImageRepository accommodationImageRepository, AwsS3Utils awsS3Utils) {
        this.accommodationImageRepository = accommodationImageRepository;
        this.awsS3Utils = awsS3Utils;
    }

    @Transactional
    public void updateAccommodationImages(Long accommodationId, List<MultipartFile> newImages, List<String> existingImageUrls) throws IOException {
        // 새 이미지가 있을 경우에만 기존 이미지 삭제
        if (newImages != null && !newImages.isEmpty()) {
            deleteExistingImages(existingImageUrls);
        }

        // 새 이미지 저장
        saveAccommodationImages(accommodationId, newImages);
    }

    // 기존 이미지 삭제 ( 메타 데이터 삭제 + s3 이미지 삭제)
    private void deleteExistingImages(List<String> existingImageUrls) throws IOException {
        if (existingImageUrls != null && !existingImageUrls.isEmpty()) {
            for (String imageUrl : existingImageUrls) {
                // 데이터베이스에서 이미지 메타데이터 삭제
                deleteImageMetadata(imageUrl);

                // S3에서 이미지 삭제
                awsS3Utils.deleteImageFromUrl(imageUrl);
                log.info("Deleted image from S3: {}", imageUrl); // 삭제 로그 추가
            }
        }
    }

    // 데이터베이스에서 이미지 메타데이터 삭제 메서드
    private void deleteImageMetadata(String imageUrl) {
        // URL에서 accommodationId 추출 (URL 형식에 따라 조정 필요)
        accommodationImageRepository.deleteByImageUrl(imageUrl);
        log.info("Deleted image from metadata: {}", imageUrl);
    }


    // s3에 올리고 메타데이터 저장
    public void saveAccommodationImages(Long accommodationId, List<MultipartFile> images) throws IOException {
        if (images != null && !images.isEmpty()) {
            List<AccommodationImage> accommodationImages = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
                String imageUrl = uploadImageToS3(accommodationId, images.get(i));
                AccommodationImage accommodationImage = createAccommodationImage(accommodationId, imageUrl, i);
                accommodationImages.add(accommodationImage);
            }

            accommodationImageRepository.saveAll(accommodationImages);
        }
    }

    // S3에 이미지 업로드
    private String uploadImageToS3(Long accommodationId, MultipartFile image) throws IOException {
        return awsS3Utils.uploadImageToFolder(IMAGE_FOLDER, accommodationId, image);
    }

    // 메타데이터 생성
    private AccommodationImage createAccommodationImage(Long accommodationId, String imageUrl, int order) {
        return AccommodationImage.builder()
            .accommodationId(accommodationId)
            .imageUrl(imageUrl)
            .imageOrder(order)
            .build();
    }


}
