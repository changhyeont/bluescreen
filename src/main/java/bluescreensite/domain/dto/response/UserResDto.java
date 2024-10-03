package bluescreensite.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResDto {

    // Getters and setters
    private Long id;
    private String username;
    private String email;


    public UserResDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
