package com.example.bluescreen.controller;

import com.example.bluescreen.dto.ScheduleRequest;
import com.example.bluescreen.dto.ScheduleResponse;
import com.example.bluescreen.model.Schedule;
import com.example.bluescreen.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "일정 관리", description = "일정 CRUD API")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(
        summary = "새 일정 생성",
        description = "새로운 일정을 생성합니다. 로그인이 필요합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "일정 생성 성공",
                content = @Content(schema = @Schema(implementation = ScheduleResponse.class))
            )
        }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> createSchedule(
            @Valid @RequestBody ScheduleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Schedule schedule = scheduleService.createSchedule(request, userDetails.getUsername());
        return ResponseEntity.ok(ScheduleResponse.from(schedule));
    }

    @Operation(
        summary = "일정 수정",
        description = "기존 일정을 수정합니다. 본인이 작성한 일정만 수정 가능합니다."
    )
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @Parameter(description = "일정 ID") @PathVariable String id,
            @Valid @RequestBody ScheduleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Schedule schedule = scheduleService.updateSchedule(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ScheduleResponse.from(schedule));
    }

    @Operation(
        summary = "일정 삭제",
        description = "기존 일정을 삭제합니다. 본인이 작성한 일정만 삭제 가능합니다."
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "일정 ID") @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        scheduleService.deleteSchedule(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "일정 목록 조회",
        description = "본인이 작성한 일정 목록을 조회합니다."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleResponse>> getSchedules(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<Schedule> schedules = scheduleService.getSchedules(userDetails.getUsername());
        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "일정 상세 조회",
        description = "본인이 작성한 일정을 상세하게 조회합니다."
    )
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ScheduleResponse> getSchedule(
            @Parameter(description = "일정 ID") @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Schedule schedule = scheduleService.getSchedule(id, userDetails.getUsername());
        return ResponseEntity.ok(ScheduleResponse.from(schedule));
    }

    @Operation(
        summary = "월간 일정 조회",
        description = "본인이 작성한 월간 일정을 조회합니다."
    )
    @GetMapping("/monthly")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ScheduleResponse>> getMonthlySchedules(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<Schedule> schedules = scheduleService.getMonthlySchedules(year, month, userDetails.getUsername());
        List<ScheduleResponse> responses = schedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
} 