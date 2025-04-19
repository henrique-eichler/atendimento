# Etapa 1: build do front-end com Node.js
FROM node:20-alpine AS frontend-builder

WORKDIR /app

COPY src/main/webapp/ ./webapp/
WORKDIR /app/webapp

RUN npm install && npm run build

# Etapa 2: build do back-end com Maven
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
RUN apt-get update && apt-get install -y maven && apt-get clean

COPY . .

# Copia o front-end compilado para dentro do static do Spring
RUN rm -rf src/main/resources/static/*
COPY --from=frontend-builder /app/webapp/dist/ src/main/resources/static/

RUN mvn clean package -DskipTests

# Etapa 3: imagem final apenas com JRE
FROM eclipse-temurin:21-jre

RUN useradd -ms /bin/bash clinica
WORKDIR /home/clinica/app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
USER clinica
ENTRYPOINT ["java", "-jar", "app.jar"]
