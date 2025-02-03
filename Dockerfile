# Використовуємо OpenJDK 17 на базі Alpine
FROM openjdk:17-jdk-alpine as builder
WORKDIR /app

# Встановлюємо Maven (щоб не використовувати mvnw)
RUN apk add --no-cache maven

# Копіюємо всі файли проєкту в контейнер
COPY . .

# Збираємо застосунок всередині контейнера
RUN mvn clean package -DskipTests

# Створюємо фінальний легковаговий контейнер
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Копіюємо зібраний JAR з попереднього етапу
COPY --from=builder /app/target/*.jar app.jar

# Вказуємо команду для запуску застосунку
ENTRYPOINT ["java", "-jar", "app.jar"]

# Відкриваємо порти для програми та дебагу
EXPOSE 8080
EXPOSE 5005
