package bluescreensite.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import bluescreensite.entity.UserRole;  // 이 줄을 추가합니다

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReqDto {

    // Getters and setters
    private String username;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private String department;
    private Integer grade;
    private UserRole role;

    public UserReqDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
