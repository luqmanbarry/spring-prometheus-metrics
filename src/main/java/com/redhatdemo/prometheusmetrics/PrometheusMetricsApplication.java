package com.redhatdemo.prometheusmetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PrometheusMetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrometheusMetricsApplication.class, args);
	}

	@Bean
	public Map<String, Object> localDataStore() {
		return new HashMap<>(TodoAppConstants.MAX_LOCAL_DB_SIZE);
	}

}
