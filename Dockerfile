# ============================================
# 1단계: Gradle로 빌드
# ============================================
FROM gradle:7.6.0-jdk17 AS builder

WORKDIR /workspace/app

# Gradle 관련 파일 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# 나머지 소스 코드 복사
COPY src src

# 빌드
RUN ./gradlew build -x test --no-daemon


# ============================================
# 2단계: 실행(Stage)
# ============================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# 1단계에서 만든 jar 파일 복사
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
