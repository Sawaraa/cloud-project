# Step 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Копіюємо pom.xml і залежності для кешування
COPY pom.xml .
RUN mvn dependency:go-offline

# Копіюємо весь код і збираємо jar
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Копіюємо готовий jar з попереднього етапу
COPY --from=build /app/target/*.jar app.jar

# Відкриваємо порт, на якому сервіс слухає
EXPOSE 8081

# Запуск сервісу
ENTRYPOINT ["java", "-jar", "app.jar"]
