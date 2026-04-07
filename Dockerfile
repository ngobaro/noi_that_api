# =========================
# Stage 1: Build ứng dụng
# =========================
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Build project (bỏ test cho nhanh)
RUN gradle build -x test


# =========================
# Stage 2: Chạy ứng dụng
# =========================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy file jar từ stage build
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port (Render sẽ override bằng biến PORT)
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]