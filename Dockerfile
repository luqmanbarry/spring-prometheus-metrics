FROM registry.access.redhat.com/ubi8/openjdk-11:latest
MAINTAINER luqmanbarry
COPY target/prometheus-metrics-1.0.0.jar metrics-app.jar
USER 1001
ENTRYPOINT ["java","-jar","/metrics-app.jar"]