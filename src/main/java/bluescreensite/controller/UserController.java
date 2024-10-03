package bluescreensite.controller;

import bluescreensite.domain.dto.request.UserReqDto;
import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResDto> registerUser(@RequestBody UserReqDto userReqDto) {
        UserResDto userResDto = userService.registerUser(userReqDto);
        return ResponseEntity.ok(userResDto);
    }
}
