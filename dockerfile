# 1. OpenJDK 기반 이미지를 사용
FROM openjdk:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트의 모든 파일을 Docker 컨테이너로 복사
COPY . .

# 4. Gradle을 사용해 프로젝트 빌드
RUN ./gradlew build

# 5. 빌드된 JAR 파일을 실행하는 명령어
CMD ["java", "-jar", "build/libs/bluescreen-0.0.1-SNAPSHOT.jar"]


