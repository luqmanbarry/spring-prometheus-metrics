package com.redhatdemo.prometheusmetrics;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class TodoRestController {

  private Map<String, Object> localDataStore;
  private String applicationName;

  TodoRestController(
      @Qualifier("localDataStore") Map<String, Object> localDataStore,
      @Value("${spring.application.name}") String applicationName) {
    this.localDataStore = localDataStore;
    this.applicationName = applicationName;
  }

  @Counted(value = "prometheus-metrics:createTodo_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:createTodo_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping("/todos/add")
  Todo createTodo() {
    Todo input =
        Todo.builder()
            .createdAt(LocalDateTime.now())
            .id(localDataStore.size() + 1)
            .name(RandomStringUtils.randomAlphabetic(5))
            .description(RandomStringUtils.randomAlphabetic(10))
            .build();
    localDataStore.put(String.valueOf(input.getId()), input);
    return input;
  }

  @Counted(value = "prometheus-metrics:getAllTodos_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:getAllTodos_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping("/todos")
  List getAllTodos() {
    if (localDataStore.isEmpty()) {
      return Collections.emptyList();
    }
    return Arrays.asList(localDataStore.values().toArray());
  }

  @Counted(value = "prometheus-metrics:welcome_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:welcome_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping
  public String welcome() {
    return "{\"message\": \"Welcome to Monitoring with Prometheus demo app.\", "
        + "\"endpoints\": [\"/actuator\", \"/actuator/prometheus\", \"/todos\", \"/todos/add\", \"/todos/{id}\"]}";
  }

  @Counted(value = "prometheus-metrics:getTodoById_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:getTodoById_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping("/todos/{id}")
  public Object getTodoById(@PathVariable("id") int id) {
    return localDataStore.get(id);
  }
}
