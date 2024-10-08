package bluescreensite.controller;

import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.entity.Post;
import bluescreensite.entity.UserRole;
import bluescreensite.entity.CalendarEvent; // CalendarEvent import 추가
import bluescreensite.entity.User;  // User 엔티티 import 추가
import bluescreensite.service.PostService;
import bluescreensite.service.UserService;
import bluescreensite.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CalendarService calendarService;

    @GetMapping
    public String adminDashboard() {
        return "admin/blueScreenHost"; // admin/dashboard.html 템플릿을 반환
    }

    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Long id, Model model) {
        UserResDto user = userService.getUserById(id);
        List<Post> userPosts = postService.getPostsByUser(id);
        List<CalendarEvent> userEvents = calendarService.getEventsByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("posts", userPosts);
        model.addAttribute("events", userEvents);
        model.addAttribute("roles", UserRole.values());
        return "admin/user-details";
    }

    @PostMapping("/user/{id}/update-role")
    public String updateUserRole(@PathVariable Long id, @RequestParam UserRole role) {
        userService.updateUserRole(id, role);
        return "redirect:/admin/user/" + id;
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable String id, @RequestParam Long userId) {
        try {
            postService.deletePostById(id);
            logger.info("관리자가 게시물 ID {}를 삭제했습니다", id);
            return "redirect:/admin/user/" + userId;
        } catch (Exception e) {
            logger.error("관리자가 게시물 삭제 중 오류 발생. 게시물 ID: {}", id, e);
            return "redirect:/admin/user/" + userId + "?error=게시물 삭제 실패";
        }
    }

    @PostMapping("/user/{id}/delete-posts")
    public String deleteUserPosts(@PathVariable Long id) {
        postService.deleteAllPostsByUser(id);
        return "redirect:/admin/user/" + id;
    }

    @PostMapping("/user/{id}/delete-events")
    public String deleteUserEvents(@PathVariable Long id) {
        User user = userService.getUserEntityById(id);
        calendarService.deleteAllEventsByUser(user.getUsername());
        return "redirect:/admin/user/" + id;
    }

    @GetMapping("/blueScreenhost")
    public String blueScreenhost(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/blueScreenhost";
    }

    @PostMapping("/event/{id}/delete")
    public String deleteEvent(@PathVariable String id) {
        calendarService.deleteEventById(id);
        return "redirect:/admin/blueScreenhost";
    }
}
