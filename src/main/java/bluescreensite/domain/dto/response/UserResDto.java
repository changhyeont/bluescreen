package bluescreensite.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import bluescreensite.entity.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class UserResDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String department;
    private Integer grade;
    private UserRole role;
    private String rankName;
    private String roleDescription;

    public UserResDto(Long id, String username, String email, String name, String phoneNumber, String department, Integer grade, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.grade = grade;
        this.role = role;
        if (role != null) {
            this.rankName = role.getRank();
            this.roleDescription = role.getDescription();
        }
    }
}
