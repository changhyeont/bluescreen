package bluescreensite.controller;

import bluescreensite.entity.Activity;
import bluescreensite.service.ActivityService;
import bluescreensite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/myactivities")
public class ActivitiesController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showMyActivities(Authentication authentication, Model model) {
        String username = authentication.getName();
        Long userId = userService.getUserByUsername(username).getId();
        List<Activity> activities = activityService.getUserActivities(userId);
        model.addAttribute("activities", activities);
        return "activities";
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> addActivity(Authentication authentication,
                                         @RequestParam("title") String title,
                                         @RequestParam(value = "url", required = false) String url,
                                         @RequestParam("content") String content,
                                         @RequestParam("images") List<MultipartFile> images) {
        String username = authentication.getName();
        Activity activity = activityService.addActivity(username, title, url, content, images);
        System.out.println("Saved activity: " + activity);
        System.out.println("Image URLs: " + activity.getImageUrls());
        return ResponseEntity.ok(activity);
    }

    @DeleteMapping("/api/{activityId}")
    @ResponseBody
    public ResponseEntity<?> deleteActivity(@PathVariable String activityId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Long userId = userService.getUserByUsername(username).getId();
            activityService.deleteActivity(activityId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("활동을 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    @GetMapping("/{activityId}")
    public String showActivityDetail(@PathVariable String activityId, Model model, Authentication authentication) {
        Activity activity = activityService.getActivityById(activityId);
        model.addAttribute("activity", activity);
        return "activity-detail";
    }
}
