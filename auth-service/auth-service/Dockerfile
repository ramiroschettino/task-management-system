# Build stage con cache de dependencias
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Copia solo el POM primero (para cachear dependencias)
COPY pom.xml .

# 2. Descarga dependencias (se cachea si el POM no cambia)
RUN mvn dependency:go-offline -B

# 3. Copia el código fuente
COPY src ./src

# 4. Compila el proyecto (con paralelización)
RUN mvn clean package -DskipTests -T 1C

# Runtime stage (imagen liviana)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia solo el JAR construido
COPY --from=build /app/target/auth-service-*.jar app.jar

# Configuración de salud y puerto
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

# Ejecución optimizada para contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]