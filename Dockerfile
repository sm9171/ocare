# 멀티스테이지 빌드를 사용한 최적화된 Spring Boot 애플리케이션 도커 이미지
FROM gradle:8-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 빌드 파일 복사
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# 의존성 다운로드 (캐시 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src/ src/

# 애플리케이션 빌드 (테스트 스킵으로 빌드 시간 단축)
RUN ./gradlew clean bootJar --no-daemon -x test

# 런타임 스테이지
FROM eclipse-temurin:17-jre-alpine

# 애플리케이션 사용자 생성 (보안 강화)
RUN addgroup -g 1000 appgroup && \
    adduser -D -s /bin/sh -u 1000 -G appgroup appuser

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 애플리케이션 소유권 변경
RUN chown appuser:appgroup app.jar

# 비 루트 사용자로 전환
USER appuser

# 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]