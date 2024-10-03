package bluescreensite.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserReqDto {

    // Getters and setters
    private String username;
    private String password;
    private String email;


    public UserReqDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
