package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.util.AwsS3Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Service
@RequiredArgsConstructor
public class CommunityImageService {

    private final AwsS3Utils awsS3Utils; // S3 유틸리티 클래스 사용

    public String imageUpload(MultipartRequest request) throws IOException {
        // 요청에서 이미지 파일 추출
        MultipartFile file = request.getFile("upload");

        // 파일 이름과 확장자 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        // 유일한 파일명을 위한 UUID 생성
        String uuidFileName = UUID.randomUUID() + ext;

        // S3에 이미지 파일 업로드
        awsS3Utils.uploadImage(uuidFileName, file);

        // 업로드된 이미지의 S3 URL 반환
        return awsS3Utils.getImageUrl(uuidFileName);
    }

    // s3 임시 위치 지정
    public String imageTemporarySave(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");

        // 파일을 임시 저장소에 저장 (예: "/tmp" 디렉토리)
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path tempFile = Files.createTempFile("temp", fileName);
        file.transferTo(tempFile.toFile());

        // 업로드된 임시 파일 경로 반환
        return tempFile.toString();
    }


}
