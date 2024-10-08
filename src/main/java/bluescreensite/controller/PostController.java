package bluescreensite.controller;

import bluescreensite.entity.Post;
import bluescreensite.entity.User;
import bluescreensite.repository.UserRepository;
import bluescreensite.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.util.List;

@Controller
@RequestMapping("/")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/community")
    public String showCommunity(Model model) {
        try {
            List<Post> posts = postService.getAllPostsWithUserInfo();
            model.addAttribute("posts", posts);
            return "community";
        } catch (Exception e) {
            logger.error("Error showing community page", e);
            model.addAttribute("error", "커뮤니티 페이지를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @GetMapping("/api/images/{filename}")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> serveFile(@PathVariable String filename) throws java.io.IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(filename)));
        GridFsResource resource = gridFsTemplate.getResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMetadata().get("_contentType").toString()))
                .body(resource);
    }

    @PostMapping(value = "/api/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<?> createPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam(value = "image", required = false) MultipartFile image,
                                        Authentication authentication) {
        try {
            String username = authentication.getName();
            Post createdPost = postService.createPost(title, content, image, username);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            logger.error("게시물 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body("게시물을 생성할 수 없습니다: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/posts/{id}")  // 이 부분을 수정
    @ResponseBody  // 이 어노테이션을 추가
    public ResponseEntity<?> deletePost(@PathVariable String id, Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
            postService.deletePost(id, user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("게시물 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body("게시물을 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable String id, Model model, Authentication authentication) {
        try {
            Post post = postService.getPostById(id);
            logger.info("Retrieved post: {}", post);  // 로그 추가
            if (post == null) {
                logger.warn("Post not found for id: {}", id);  // 로그 추가
                model.addAttribute("error", "게시물을 찾을 수 없습니다.");
                return "error";
            }
            model.addAttribute("post", post);
            if (authentication != null) {
                model.addAttribute("currentUsername", authentication.getName());
            }
            return "post-detail";
        } catch (Exception e) {
            logger.error("게시물 조회 중 오류 발생", e);
            model.addAttribute("error", "게시물을 조회하는 중 오류가 발생했습니다: " + e.getMessage());
            return "error";
        }
    }
}
