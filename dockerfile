# 1. OpenJDK 기반 이미지를 사용
FROM openjdk:17-jdk-alpine

# 2. 컨테이너에서 사용할 디렉토리 생성
VOLUME /tmp

# 3. Gradle 빌드 후 생성된 JAR 파일을 복사
COPY build/libs/bluescreen-0.0.1-SNAPSHOT.jar app.jar

# 4. JAR 파일을 실행하는 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]
