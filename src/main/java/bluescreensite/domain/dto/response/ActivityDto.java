package bluescreensite.domain.dto.response;

import bluescreensite.entity.Activity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ActivityDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private LocalDateTime createdAt;
    private String username;
    private List<String> imageUrls;

    public ActivityDto(Activity activity) {
        this.id = activity.getId();
        this.title = activity.getTitle();
        this.content = activity.getContent();
        this.url = activity.getUrl();
        this.createdAt = activity.getCreatedAt();
        this.username = activity.getUser().getUsername();
        this.imageUrls = activity.getImageUrls();
    }
}