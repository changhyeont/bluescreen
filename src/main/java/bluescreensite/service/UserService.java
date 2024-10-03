package bluescreensite.service;

import bluescreensite.domain.dto.request.UserReqDto;
import bluescreensite.domain.dto.response.UserResDto;
import bluescreensite.entity.User;
import bluescreensite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResDto registerUser(UserReqDto userReqDto) {
        User user = new User(userReqDto.getUsername(), userReqDto.getPassword(), userReqDto.getEmail());
        User savedUser = userRepository.save(user);
        return new UserResDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
