package com.example.bluescreen.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "schedules")
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String userId;
    private String username;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 