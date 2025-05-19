FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -T 1C

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/auth-service-*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

# Ejecución optimizada para contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]