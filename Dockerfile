FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system streamflix && useradd --system --gid streamflix streamflix

COPY --from=build /app/target/streamflix-0.0.1-SNAPSHOT.jar app.jar

USER streamflix
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
