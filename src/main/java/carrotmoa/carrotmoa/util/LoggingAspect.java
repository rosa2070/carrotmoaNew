package carrotmoa.carrotmoa.util;

import carrotmoa.carrotmoa.model.request.RequestDTO;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* carrotmoa.carrotmoa.controller.api.*.*(..)) && args(requestDTO,..)")
    public void logBefore(JoinPoint joinPoint, RequestDTO requestDTO) {
        log.info("메서드 실행 전 - {}", joinPoint.getSignature().getName());
        log.info("Request DTO 클래스: {}", requestDTO.getClass().getSimpleName());
        log.info("파라미터 값: {}", Arrays.toString(requestDTO.getClass().getDeclaredFields()));

        // 필드 정보를 로그에 기록
        logFields(requestDTO);
    }

    private void logFields(RequestDTO requestDTO) {
        // RequestDTO의 모든 필드를 순회하며 로그 기록
        Arrays.stream(requestDTO.getClass().getDeclaredFields())
            .forEach(field -> {
                field.setAccessible(true); // private 필드 접근 허용
                try {
                    Object value = field.get(requestDTO);  // 필드의 값을 가져옴
                    if (value instanceof MultipartFile) {
                        logMultipartFileField((MultipartFile) value);
                    } else if (value instanceof List) {
                        logListField((List<?>) value, field.getName());
                    } else {
                        log.info("{} ({}): {}", field.getName(), field.getType().getSimpleName(), value);
                    }
                } catch (IllegalAccessException e) {
                    log.warn("필드 값을 읽는 중 오류 발생: {}", field.getName());
                }
            });
    }

    private void logMultipartFileField(MultipartFile file) {
        if (file != null) {
            String anonymizedFileName = "file_" + System.currentTimeMillis() + ".jpg"; // 파일명 익명화
            log.info("Uploaded File - Name: {}, Size: {}, ContentType: {}",
                anonymizedFileName, file.getSize(), file.getContentType());
        }
    }

    private void logListField(List<?> list, String fieldName) {
        if (list != null && !list.isEmpty()) {
            log.info("{} (List):", fieldName); // 리스트 필드 이름 로그 기록
            for (Object item : list) {
                logListItem(item); // 리스트 항목 로그 기록
            }
        }
    }

    private void logListItem(Object item) {
        if (item instanceof MultipartFile) {
            logMultipartFileField((MultipartFile) item);
        } else if (item instanceof RequestDTO) {
            logFields((RequestDTO) item); // RequestDTO로 재귀적으로 필드 로그 기록
        } else {
            log.info("List Item: {}", item);
        }
    }
}
