package bluescreensite.service;

import bluescreensite.entity.CalendarEvent;
import bluescreensite.entity.User;  // User 엔티티 import 추가
import bluescreensite.repository.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Lazy
    @Autowired
    private UserService userService;

    // UserService 주입 제거

    public CalendarEvent addEvent(CalendarEvent event, String username, String authorName) {
        event.setUsername(username);
        event.setAuthorName(authorName);
        return calendarEventRepository.save(event);
    }

    public List<CalendarEvent> getAllEvents() {
        return calendarEventRepository.findAll();
    }

    public CalendarEvent getEventById(String id) {
        return calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public boolean deleteEvent(String id, String username) {
        CalendarEvent event = getEventById(id);
        if (event.getUsername().equals(username)) {
            calendarEventRepository.delete(event);
            return true;
        }
        return false;
    }

    public List<CalendarEvent> getEventsByUser(Long userId) {
        User user = userService.getUserEntityById(userId);
        return calendarEventRepository.findByUsername(user.getUsername());
    }

    @Transactional
    public void deleteAllEventsByUser(String username) {
        calendarEventRepository.deleteByUsername(username);
    }

    public void deleteEventById(String id) {
        calendarEventRepository.deleteById(id);
    }
}
