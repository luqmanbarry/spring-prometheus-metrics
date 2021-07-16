package com.redhatdemo.prometheusmetrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromMeterRegistryConfig {
  @Value("${spring.application.name}")
  String appName;

  @Value("${spring.application.profiles.active}")
  String environment;

  @Value("${HOSTNAME:1}")
  String instanceId;

  @Bean
  MeterRegistryCustomizer<PrometheusMeterRegistry> configureMetricsRegistry() {
    return registry ->
        registry
            .config()
            .commonTags("appName", appName, "env", environment, "instanceId", instanceId);
  }

}
