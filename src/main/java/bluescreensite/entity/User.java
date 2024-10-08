package bluescreensite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")  // 'user'는 예약어일 수 있으므로 'users'로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)  // 이 부분을 확인하세요
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Integer grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // username, password, email을 매개변수로 받는 생성자 추가
    public User(String username, String password, String email, String name, String phoneNumber, String department, Integer grade, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.grade = grade;
        this.role = role;
    }

    public User(String username) {
        this.username = username;
    }

    // getter와 setter 추가
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
