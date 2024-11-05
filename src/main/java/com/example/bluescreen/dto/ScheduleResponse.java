package com.example.bluescreen.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.bluescreen.model.Schedule;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ScheduleResponse {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
    private String username;  // 작성자 이름
    private boolean isPublic;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ScheduleResponse from(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(schedule.getId());
        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setDateTime(schedule.getDateTime());
        response.setUsername(schedule.getUsername());
        response.setPublic(schedule.isPublic());
        
        // 날짜/시간 분리
        response.setYear(schedule.getDateTime().getYear());
        response.setMonth(schedule.getDateTime().getMonthValue());
        response.setDay(schedule.getDateTime().getDayOfMonth());
        response.setHour(schedule.getDateTime().getHour());
        response.setMinute(schedule.getDateTime().getMinute());
        
        response.setCreatedAt(schedule.getCreatedAt());
        response.setUpdatedAt(schedule.getUpdatedAt());
        
        return response;
    }
} 