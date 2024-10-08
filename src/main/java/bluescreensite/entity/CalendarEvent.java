package bluescreensite.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CalendarEvent {

    @Id
    private String id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Seoul")
    private Date dateTime;

    private String description;
    private String username;
    private String authorName; // 작성자 이름 추가
}
