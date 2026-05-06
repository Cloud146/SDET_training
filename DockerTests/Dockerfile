FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
COPY testng.xml .

RUN mvn dependency:go-offline

COPY src ./src

CMD ["mvn", "test"]