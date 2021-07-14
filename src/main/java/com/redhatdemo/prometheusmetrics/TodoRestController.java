package com.redhatdemo.prometheusmetrics;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class TodoRestController {

  private Map<String, Object> localDataStore;

  TodoRestController(@Qualifier("localDataStore") Map<String, Object> localDataStore) {
    this.localDataStore = localDataStore;
  }

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

  @GetMapping("/todos")
  List getAllTodos() {
    if (localDataStore.isEmpty()) {
      return Collections.emptyList();
    }
    return Arrays.asList(localDataStore.values().toArray());
  }

  @GetMapping
  public String welcome() {
    return "{\"message\": \"Welcome to Monitoring with Prometheus demo app.\", " +
            "\"endpoints\": [\"/actuator\", \"/actuator/prometheus\", \"/todos\", \"/todos/add\", \"/todos/{id}\"]}";
  }

  @GetMapping("/todos/{id}")
  public Object findById(@PathVariable("id") int id) {
    return localDataStore.get(id);
  }
}
