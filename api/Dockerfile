# # # # # # # Build Stage # # # # # # # #
FROM amazoncorretto:21.0.2-alpine3.19 as build

WORKDIR /zapcode

COPY . .

RUN ./gradlew --no-daemon build

# # # # # # # Runtime Stage # # # # # # # #
FROM amazoncorretto:21.0.2-alpine3.19

WORKDIR /api

COPY --from=build /zapcode/api/build/libs/api-all.jar /api/api-all.jar

CMD ["java", "-jar", "/api/api-all.jar"]
