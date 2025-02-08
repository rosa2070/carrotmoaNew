FROM openjdk:17

# Gradle 빌드 후 생성된 JAR 파일을 Docker 이미지로 복사
#COPY ./build/libs/carrot-moa-0.0.1-SNAPSHOT.jar /app.jar
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
# JASYPT 비밀번호를 환경 변수로 설정하지 않음 (보안상 문제)
# ENV 및 ARG 사용을 피하는 것이 좋습니다.

# JASYPT 비밀번호를 전달하며 애플리케이션 실행
#ENTRYPOINT ["java", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "-jar", "/app.jar"]
ENTRYPOINT ["java", "-jar", "/app.jar"]