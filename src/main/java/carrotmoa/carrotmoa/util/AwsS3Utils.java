package carrotmoa.carrotmoa.util;


import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Utils {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 사용자가 업로드한 이미지를 지정된 S3 폴더에 업로드
    public String uploadImageToFolder(String folderName, Long id, MultipartFile file) throws IOException {
        String fileName = folderName + "/" + id + "/" + UUID.randomUUID() + getFileExtension(file.getOriginalFilename());

        // S3에 이미지 업로드
        uploadImage(fileName, file);

        // 업로드된 이미지 URL 반환
        String imageUrl = getImageUrl(fileName);

        // 성공적으로 저장된 경우 로그 출력
        log.info("Uploaded image to S3: {}", imageUrl);

        return imageUrl; // 업로드된 이미지 URL 반환
    }

    // getBytes와 fromInputStream()의 차이 공부
    public void uploadImage(String fileName, MultipartFile file) {
        try {
            // S3에 업로드할 객체 요청을 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // 업로드할 S3 버킷의 이름
                .key(fileName) // S3에서의 파일 이름 (경로 포함)
                .contentType(file.getContentType()) // 파일의 콘텐츠 타입 (예: image/jpeg)
                .build();

            // s3에 파일 업로드
            s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()) // InputStream으로부터 RequestBody를 생성, 실제 파일 데이터 포함
            );

            log.info("Uploaded image: {}", fileName);

        } catch (IOException e) {
            log.error("Error uploading image: {}", fileName, e);
            throw new RuntimeException("Error uploading file: " + fileName, e);
        }
    }

    // 파일 확장자를 추출하는 메서드
    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 업로드된 이미지의 URL을 반환하는 메서드
    public String getImageUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    // 폴더로 만든 경우
    public void deleteImageFromUrl(String url) throws IOException {
        // URL에서 파일 경로 추출
        String filePath = extractFilePathFromUrl(url);

        // S3에서 객체 삭제 요청 생성
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
            .bucket(bucketName) // 버킷 이름
            .key(filePath) // 파일 경로
            .build();

        try {
            s3Client.deleteObject(deleteObjectRequest); // S3에서 객체 삭제
            log.info("Deleted image: {}", filePath); // 삭제 성공 로그
        } catch (SdkException e) {
            log.error("Error deleting file from S3: {}", filePath, e); // 삭제 실패 로그
            throw new IOException("Error deleting file from S3: " + filePath, e); // 예외 발생
        }
    }

    private String extractFilePathFromUrl(String S3url) {
        // URL을 슬래시('/')로 분리
        String[] parts = S3url.split("/");

        // room/42/와 파일 이름을 결합
        return parts[parts.length - 3] + "/" + parts[parts.length - 2] + "/" + parts[parts.length - 1];
    }


}
