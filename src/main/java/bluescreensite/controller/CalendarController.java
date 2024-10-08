package bluescreensite.controller;

import bluescreensite.entity.CalendarEvent;
import bluescreensite.entity.User;
import bluescreensite.service.CalendarService;
import bluescreensite.service.UserService;  // UserService 주입
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.TimeZone;  // 이 줄을 추가해 주세요

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private UserService userService;  // UserService 주입

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @GetMapping
    public String showCalendar(Model model) {
        return "calendar";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<Object> getEvents() {
        List<CalendarEvent> events = calendarService.getAllEvents();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return events.stream().map(event -> {
            return new Object() {
                public String id = event.getId();
                public String title = event.getTitle();
                public String start = outputFormat.format(event.getDateTime());
                public String description = event.getDescription();
                public String username = event.getUsername();
                public String authorName = event.getAuthorName();
            };
        }).collect(Collectors.toList());
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addEvent(@RequestBody CalendarEvent event, Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);  // 사용자 정보 가져오기
            logger.info("Received event: {}", event);
            
            // 이미 Date 객체로 변환되어 있으므로 추가 변환 불필요
            // event.getDateTime()은 이미 Date 객체임
            
            CalendarEvent savedEvent = calendarService.addEvent(event, username, user.getName());
            logger.info("Saved event: {}", savedEvent);
            
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Object responseEvent = new Object() {
                public String id = savedEvent.getId();
                public String title = savedEvent.getTitle();
                public String start = outputFormat.format(savedEvent.getDateTime());
                public String description = savedEvent.getDescription();
                public String username = savedEvent.getUsername();
                public String authorName = savedEvent.getAuthorName();
            };
            logger.info("Response event: {}", responseEvent);
            return ResponseEntity.ok(responseEvent);
        } catch (Exception e) {
            logger.error("Error adding event", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEvent(@PathVariable String id, Authentication authentication) {
        try {
            String username = authentication.getName();
            boolean deleted = calendarService.deleteEvent(id, username);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("이벤트를 삭제할 권한이 없습니다.");
            }
        } catch (RuntimeException e) {
            logger.error("이벤트 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
