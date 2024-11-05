package com.example.bluescreen.service;

import com.example.bluescreen.dto.ScheduleRequest;
import com.example.bluescreen.model.Schedule;
import com.example.bluescreen.model.User;
import com.example.bluescreen.repository.ScheduleRepository;
import com.example.bluescreen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public Schedule createSchedule(ScheduleRequest request, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
            
        Schedule schedule = new Schedule();
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        
        // LocalDateTime 생성
        LocalDateTime dateTime = LocalDateTime.of(
            request.getYear(),
            request.getMonth(),
            request.getDay(),
            request.getHour(),
            request.getMinute()
        );
        
        schedule.setDateTime(dateTime);
        schedule.setUserId(user.getId().toString());
        schedule.setUsername(user.getUsername());
        schedule.setPublic(request.isPublic());
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(String id, ScheduleRequest request, String username) {
        Schedule existingSchedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));
            
        if (!existingSchedule.getUsername().equals(username)) {
            throw new RuntimeException("일정 수정 권한이 없습니다");
        }
        
        LocalDateTime dateTime = LocalDateTime.of(
            request.getYear(),
            request.getMonth(),
            request.getDay(),
            request.getHour(),
            request.getMinute()
        );
        
        existingSchedule.setTitle(request.getTitle());
        existingSchedule.setDescription(request.getDescription());
        existingSchedule.setDateTime(dateTime);
        existingSchedule.setPublic(request.isPublic());
        existingSchedule.setUpdatedAt(LocalDateTime.now());
        
        return scheduleRepository.save(existingSchedule);
    }

    public List<Schedule> getMonthlySchedules(int year, int month, String username) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        
        return scheduleRepository.findByDateTimeBetween(startOfMonth, endOfMonth)
            .stream()
            .filter(schedule -> schedule.isPublic() || schedule.getUsername().equals(username))
            .collect(Collectors.toList());
    }

    // 일정 삭제
    public void deleteSchedule(String id, String username) {
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));
            
        if (!schedule.getUsername().equals(username)) {
            throw new RuntimeException("일정 삭제 권한이 없습니다");
        }
        
        scheduleRepository.deleteById(id);
    }

    // 사용자의 모든 일정 조회
    public List<Schedule> getSchedules(String username) {
        return scheduleRepository.findByUsername(username);
    }

    // 특정 일정 상세 조회
    public Schedule getSchedule(String id, String username) {
        Schedule schedule = scheduleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));
            
        if (!schedule.isPublic() && !schedule.getUsername().equals(username)) {
            throw new RuntimeException("일정 조회 권한이 없습니다");
        }
        
        return schedule;
    }
} 