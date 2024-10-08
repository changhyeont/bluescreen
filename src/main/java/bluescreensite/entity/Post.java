package bluescreensite.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String id;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long authorId; // 이 부분이 데이터베이스의 필드 이름과 일치하는지 확인
    private String authorUsername;
    private String authorName; // 작성자의 실제 이름을 저장할 필드 추가
    private String imageUrl;

    // User 객체를 저장하기 위한 필드 추가
    private User author;

    public void setAuthor(User user) {
        this.author = user;
        this.authorId = user.getId();
        this.authorUsername = user.getUsername();
        this.authorName = user.getName(); // 작성자의 실제 이름 설정
    }
}
