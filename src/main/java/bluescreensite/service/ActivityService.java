package bluescreensite.service;

import bluescreensite.entity.Activity;
import bluescreensite.entity.User;
import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public Activity addActivity(String username, String title, String url, String content, List<MultipartFile> images) {
        User user = userService.getUserByUsername(username);
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setTitle(title);
        activity.setUrl(url);
        activity.setContent(content);
        activity.setCreatedAt(LocalDateTime.now());

        List<String> imageUrls = saveImages(images);
        activity.setImageUrls(imageUrls);

        return activityRepository.save(activity);
    }

    public List<Activity> getUserActivities(Long userId) {
        User user = userService.getUserEntityById(userId);
        return activityRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public void deleteActivity(String activityId, Long userId) {
        Activity activity = getActivityById(activityId);
        if (!activity.getUserId().equals(userId) && !userService.isAdmin(userId)) {
            throw new RuntimeException("이 활동을 삭제할 권한이 없습니다");
        }
        activityRepository.delete(activity);
    }

    private List<String> saveImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        try {
            Path uploadPath = getUploadPath();
            Files.createDirectories(uploadPath);
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(filename);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    imageUrls.add(filename); // 파일 이름만 저장
                    System.out.println("이미지 저장 위치: " + filePath.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("이미지를 저장할 수 없습니다. 오류: " + e.getMessage());
        }
        return imageUrls;
    }

    public Activity getActivityById(String activityId) {
        return activityRepository.findById(Long.parseLong(activityId))
            .orElseThrow(() -> new RuntimeException("활동을 찾을 수 없습니다: " + activityId));
    }
}
