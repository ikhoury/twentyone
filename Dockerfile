FROM openjdk:11.0.9.1-jdk-buster as build
WORKDIR /source
COPY . .
RUN ./mvnw package

FROM openjdk:11.0.9.1-jre-buster
WORKDIR /app
COPY --from=build /source/target/twentyone-*-jar-with-dependencies.jar twentyone.jar
CMD ["java", "-jar", "twentyone.jar"]
