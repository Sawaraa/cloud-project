# Етап 1: Збірка (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Копіюємо pom.xml та качаємо залежності (кешуємо цей шар)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копіюємо код і збираємо jar
COPY src ./src
RUN mvn clean package -DskipTests

# Етап 2: Запуск (Runtime)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Копіюємо зібраний jar-файл з першого етапу
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]