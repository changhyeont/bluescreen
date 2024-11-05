package com.example.bluescreen.repository;

import com.example.bluescreen.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByUsername(String username);
    List<Schedule> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    void deleteByUserId(String userId);
    List<Schedule> findAllByOrderByDateTimeDesc();
} 