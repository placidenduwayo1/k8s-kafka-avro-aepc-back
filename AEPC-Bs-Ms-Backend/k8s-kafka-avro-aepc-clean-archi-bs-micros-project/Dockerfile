FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder
WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src
RUN mvn package -DskipTest
WORKDIR /build/target/
RUN java -Djarmode=layertools -jar k8s-kafka-avro-aepc-bs-ms-project.jar extract

FROM maven:3.8.6-eclipse-temurin-17-alpine
COPY --from=builder /build/target/dependencies/ ./
COPY --from=builder /build/target/spring-boot-loader/ ./
COPY --from=builder /build/target/snapshot-dependencies/ ./
COPY --from=builder /build/target/application/ ./

EXPOSE 8584
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "org.springframework.boot.loader.JarLauncher"]