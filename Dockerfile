# Використовуємо офіційний образ JDK 17
FROM openjdk:17-jdk-alpine

# Встановлюємо робочу директорію в контейнері
WORKDIR /app

# Копіюємо усі файли проєкту (Render сам виконає `git clone`)
COPY . .

# Збираємо застосунок всередині контейнера (використовуємо Maven)
RUN ./mvnw clean package -DskipTests

# Запускаємо застосунок
CMD ["java", "-jar", "target/*.jar"]

# Відкриваємо порти
EXPOSE 8080
EXPOSE 5005
