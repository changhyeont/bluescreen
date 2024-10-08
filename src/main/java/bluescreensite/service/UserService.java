package bluescreensite.service;

import bluescreensite.domain.dto.request.UserReqDto;
import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.entity.CalendarEvent;
import bluescreensite.entity.User;
import bluescreensite.entity.UserRole;  // 이 줄을 추가합니다
import bluescreensite.repository.UserRepository;
import bluescreensite.repository.ActivityRepository; // Added dependency for ActivityRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import bluescreensite.domain.dto.response.ActivityDto; // Added import for ActivityDto

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CalendarService calendarService; // Added dependency for CalendarService

    public UserResDto registerUser(UserReqDto userReqDto) {
        String encodedPassword = passwordEncoder.encode(userReqDto.getPassword());
        
        // 기본 역할을 ROLE_CLEANER로 설정
        UserRole role = (userReqDto.getRole() != null) ? userReqDto.getRole() : UserRole.ROLE_CLEANER;
        
        User user = new User(userReqDto.getUsername(), 
                             encodedPassword,
                             userReqDto.getEmail(),
                             userReqDto.getName(),
                             userReqDto.getPhoneNumber(),
                             userReqDto.getDepartment(),
                             userReqDto.getGrade(),
                             role);
        User savedUser = userRepository.save(user);
        return new UserResDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                              savedUser.getName(), savedUser.getPhoneNumber(), savedUser.getDepartment(),
                              savedUser.getGrade(), savedUser.getRole());
    }

    public UserResDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResDto(user.getId(), user.getUsername(), user.getEmail(),
                              user.getName(), user.getPhoneNumber(), user.getDepartment(),
                              user.getGrade(), user.getRole());  // user.getRole() 추가
    }

    public UserResDto updateUserInfo(String username, UserReqDto userReqDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmail(userReqDto.getEmail());
        user.setName(userReqDto.getName());
        user.setPhoneNumber(userReqDto.getPhoneNumber());
        user.setDepartment(userReqDto.getDepartment());
        user.setGrade(userReqDto.getGrade());

        if (userReqDto.getPassword() != null && !userReqDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        }

        if (userReqDto.getRole() != null) {
            user.setRole(userReqDto.getRole());
        }

        User updatedUser = userRepository.save(user);
        return new UserResDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(),
                              updatedUser.getName(), updatedUser.getPhoneNumber(), updatedUser.getDepartment(),
                              updatedUser.getGrade(), updatedUser.getRole());
    }

    public List<UserResDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResDto(user.getId(), user.getUsername(), user.getEmail(),
                        user.getName(), user.getPhoneNumber(), user.getDepartment(),
                        user.getGrade(), user.getRole()))
                .collect(Collectors.toList());
    }

    public UserResDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResDto(user.getId(), user.getUsername(), user.getEmail(),
                user.getName(), user.getPhoneNumber(), user.getDepartment(),
                user.getGrade(), user.getRole());
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void updateUserRole(Long id, UserRole role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @jakarta.transaction.Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        // 사용자의 캘린더 이벤트 삭제
        calendarService.deleteAllEventsByUser(user.getUsername());
        
        // 사용자 삭제
        userRepository.delete(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public void addEventForUser(CalendarEvent event, String username) {
        User user = getUserByUsername(username);
        calendarService.addEvent(event, username, user.getName());
    }

    public boolean isAdmin(Long userId) {
        User user = getUserEntityById(userId);
        return user != null && user.getRole() == UserRole.ROLE_ADMIN;
    }

    @Autowired
    private ActivityRepository activityRepository;

    public List<ActivityDto> getMemberActivities(Long memberId) {
        User user = getUserEntityById(memberId);
        return activityRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(ActivityDto::new)
            .collect(Collectors.toList());
    }
}

