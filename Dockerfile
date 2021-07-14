FROM registry.access.redhat.com/ubi8/openjdk-11:latest
# FROM azul/zulu-openjdk:11

# COPY --from=jhu45378.live.dynatrace.com/linux/oneagent-codemodules:java / /
# ENV LD_PRELOAD /opt/dynatrace/oneagent/agent/lib64/liboneagentproc.so

LABEL description="This is a spring project attempting to implement metrics based monitoring with Actuator, Prometheus and Dynatrace"
MAINTAINER luqmanbarry

ENV DYNATRACE_API_TOKEN="YOURAPITOKEN"
ENV DYNATRACE_URI="http://your-dynatrace-host.domain.com"
ENV DYNATRACE_GROUP="redhat-pnc-monitoring-poc"
ENV DYNATRACE_DEVICE_ID="prometheus-metrics"
ENV DYNATRACE_ENABLED="false"
ENV DYNATRACE_STEP="30s"

COPY target/prometheus-metrics-1.0.0.jar /app.jar

# USER 1001

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]