package com.example.bluescreen.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {
    private String title;
    private String content;
} 