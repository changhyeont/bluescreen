package bluescreensite.controller;

import bluescreensite.domain.dto.request.UserReqDto;
import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.domain.dto.response.ActivityDto;
import bluescreensite.entity.Activity;
import bluescreensite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller  // @RestController 대신 @Controller 사용
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;
    
    

    // GET 요청: 회원가입 페이지(register.html)를 반환
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";  // templates/register.html을 반환
    }

    // POST 요청: 회원가입 처리
    @PostMapping("/api/users/register")
    public String registerUser(@ModelAttribute UserReqDto userReqDto) {
        userService.registerUser(userReqDto);
        return "redirect:/login";  // 회원가입 후 로그인 페이지로 리다이렉트
    }

    // 로그인 폼을 반환
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 회원 정보 페이지로 이동
    @GetMapping("/myinfo")
    public String showMyInfo(Authentication authentication, Model model) {
        String username = authentication.getName();
        UserResDto userInfo = userService.getUserInfo(username);
        model.addAttribute("user", userInfo);
        return "myinfo";
    }

    // 회원 정보 업데이트 처리
    @PostMapping("/myinfo/update")
    public String updateMyInfo(Authentication authentication, @ModelAttribute UserReqDto userReqDto) {
        String username = authentication.getName();
        userService.updateUserInfo(username, userReqDto);
        return "redirect:/myinfo";
    }

    // 에러 처리
    @GetMapping("/error")
    public String handleError() {
        return "error";  // error.html 템플릿을 생성해야 합니다
    }

    // 동아리 멤버 목록을 반환
    @GetMapping("/members")
    public String showMembers(Model model) {
        List<UserResDto> members = userService.getAllUsers();
        model.addAttribute("members", members);
        return "members";
    }

    // 활동 내역을 반환
    @GetMapping("/api/members/{memberId}/activities")
    @ResponseBody
    public List<ActivityDto> getMemberActivities(@PathVariable Long memberId) {
        return userService.getMemberActivities(memberId);
    }

}
