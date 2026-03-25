# 1. Сборка (Build)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Пропускаем тесты при сборке jar, чтобы не зависеть от окружения
RUN mvn package -DskipTests

# 2. Запуск (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Копируем собранный JAR
COPY --from=build /app/target/*.jar app.jar
# Создаем папку для базы данных
RUN mkdir -p ./data

# Открываем порт
EXPOSE 8080

# Запускаем. Важно: мы можем переопределить настройки через аргументы
ENTRYPOINT ["java", "-jar", "app.jar"]