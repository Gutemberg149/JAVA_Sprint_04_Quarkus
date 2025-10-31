##FROM ubuntu:latest
##LABEL authors="gutembergrocha"
##
##ENTRYPOINT ["top", "-b"]
##
#FROM registry.access.redhat.com/ubi9/openjdk-21:1.23
#
#ENV LANGUAGE='en_US:en'
#
#
#  # We make four distinct layers so if there are application changes the library layers can be re-used
#COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
#COPY --chown=185 target/quarkus-app/*.jar /deployments/
#COPY --chown=185 target/quarkus-app/app/ /deployments/app/
#COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/
#
#EXPOSE 8080
#USER 185
#ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
#ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
#
#ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
# Stage 1: Build the Quarkus application
# Stage 1: Build the Quarkus application
# Stage 1: Build the Quarkus application
# Stage 1: Build the Quarkus application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /project

# Copy everything at once
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime image
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

ENV LANGUAGE='en_US:en'

# Copy the built application from the builder stage
COPY --from=builder --chown=185 /project/target/quarkus-app/lib/ /deployments/lib/
COPY --from=builder --chown=185 /project/target/quarkus-app/*.jar /deployments/
COPY --from=builder --chown=185 /project/target/quarkus-app/app/ /deployments/app/
COPY --from=builder --chown=185 /project/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]