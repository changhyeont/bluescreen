package bluescreensite.config;

import bluescreensite.entity.User;
import bluescreensite.entity.UserRole;
import bluescreensite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("blueScreenhost").isEmpty()) {
            User adminUser = new User(
                "blueScreenhost",
                passwordEncoder.encode("80577241"),
                "bluescreenhost@gmail.com",
                "관리자",
                "010-1234-5678",
                "관리부서",
                1,
                UserRole.ROLE_ADMIN
            );
            userRepository.save(adminUser);
            System.out.println("관리자 계정이 성공적으로 생성되었습니다.");
        } else {
            System.out.println("관리자 계정이 이미 존재합니다.");
        }
    }
}
