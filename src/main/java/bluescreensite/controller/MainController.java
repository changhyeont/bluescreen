package bluescreensite.controller;

import bluescreensite.entity.Post;
import bluescreensite.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String showMainPage(Model model) {
        List<Post> recentPosts = postService.getRecentPosts(5); // 최근 5개의 게시글을 가져옵니다
        model.addAttribute("recentPosts", recentPosts);
        return "main";
    }
}
