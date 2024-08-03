FROM gradle:8.9-jdk21 AS build

WORKDIR /exchange-rates
COPY . .
RUN gradle --no-daemon clean bootJar --exclude-task test

FROM eclipse-temurin:21-jdk

WORKDIR /exchange-rates
COPY --from=build /exchange-rates/build/libs/exchange-rates-0.0.1-SNAPSHOT.jar exchange-rates.jar

ENTRYPOINT ["java", "-jar", "/exchange-rates/exchange-rates.jar"]
