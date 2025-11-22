# 1단계: 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /workspace/app

# Gradle wrapper 전체 복사
COPY gradlew .
COPY gradle gradle

# 빌드 스크립트 복사
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew

# 소스 폴더 전체 복사
COPY src src

# 테스트 제외하고 빌드 실행
RUN ./gradlew build --no-daemon -x test --stacktrace


# 2단계: 실행
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 빌드한 jar 복사
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
