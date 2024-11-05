package com.example.bluescreen.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ScheduleRequest {
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    private String description;
    
    @NotNull(message = "년도는 필수입니다")
    @Min(2024) @Max(2100)
    private int year;
    
    @NotNull(message = "월은 필수입니다")
    @Min(1) @Max(12)
    private int month;
    
    @NotNull(message = "일은 필수입니다")
    @Min(1) @Max(31)
    private int day;
    
    @NotNull(message = "시간은 필수입니다")
    @Min(0) @Max(23)
    private int hour;
    
    @NotNull(message = "분은 필수입니다")
    @Min(0) @Max(59)
    private int minute;
    
    private boolean isPublic = true;
} 