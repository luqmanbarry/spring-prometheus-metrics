package com.redhatdemo.prometheusmetrics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {
    private int id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

}
