package com.redhatdemo.prometheusmetrics;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
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
    log.info("Start: TodoRestController.createTodo()");
    Todo input =
        Todo.builder()
            .createdAt(LocalDateTime.now())
            .id(localDataStore.size() + 1)
            .name(RandomStringUtils.randomAlphabetic(5))
            .description(RandomStringUtils.randomAlphabetic(10))
            .build();
    if (localDataStore.size() > TodoAppConstants.MAX_LOCAL_DB_SIZE) {
      localDataStore.clear();
    }
    localDataStore.put(String.valueOf(input.getId()), input);
    log.info("End: TodoRestController.createTodo()");
    stressAppMemory();
    stressAppCPU();
    return input;
  }

  @Counted(value = "prometheus-metrics:getAllTodos_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:getAllTodos_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping("/todos")
  List getAllTodos() {
    log.info("Start: TodoRestController.getAllTodos()");
    if (localDataStore.isEmpty()) {
      return Collections.emptyList();
    }
    log.info("End: TodoRestController.getAllTodos()");
    stressAppCPU();
    return Arrays.asList(localDataStore.values().toArray());
  }

  @Counted(value = "prometheus-metrics:welcome_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:welcome_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping
  public String welcome() {
    log.info("Start: TodoRestController.welcome()");
    stressAppCPU();
//    stressAppMemory();
    return "{\"message\": \"Welcome to Monitoring with Prometheus demo app.\", "
        + "\"endpoints\": [\"/actuator\", \"/actuator/prometheus\", \"/todos\", \"/todos/add\", \"/todos/{id}\"]}";
  }

  @Counted(value = "prometheus-metrics:getTodoById_call_count", description = "count of /todos endpoint calls")
  @Timed(value = "prometheus-metrics:getTodoById_call_process_time", description = "A measure of how long it takes to process fetch todos")
  @GetMapping("/todos/{id}")
  public Object getTodoById(@PathVariable("id") int id) {
    log.info("Start: TodoRestController.getTodoById()");
    return localDataStore.get(id);
  }

  private void stressAppCPU() {
    for (int i = 0; i <= 100; i++) {
      CompletableFuture.runAsync(() -> delayExecution());
    }
  }

  private void delayExecution() {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void stressAppMemory() {
    int stressLimit = 5;
    List<String> stressData = new ArrayList<>(stressLimit);
    for(int i = 0; i < stressLimit; i++) {
      stressData.add(RandomStringUtils.randomAlphabetic(1));
    }
    stressData.stream()
            .map(item -> item.toUpperCase())
            .map(item -> item.toLowerCase(Locale.ROOT))
            .collect(Collectors.toList());
  }
}
