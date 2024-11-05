package com.example.bluescreen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "boards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private Long userId;
    private String username;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
