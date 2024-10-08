package bluescreensite.service;

import bluescreensite.entity.Post;
import bluescreensite.entity.User;
import bluescreensite.repository.PostRepository;
import bluescreensite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@SuppressWarnings("unused")
@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(String title, String content, MultipartFile image, String username) {
        try {
            User author = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
            
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setAuthor(author); // 이 메서드 내에서 authorName도 설정됩니다.
            post.setAuthorId(author.getId()); // 이 부분이 있는지 확인
            post.setCreatedAt(LocalDateTime.now());
            
            if (image != null && !image.isEmpty()) {
                String filename = storeFile(image);
                post.setImageUrl("/api/images/" + filename);
            }
            
            Post savedPost = postRepository.save(post);
            logger.debug("생성된 게시물: {}", savedPost);
            return savedPost;
        } catch (Exception e) {
            logger.error("게시물 생성 중 오류", e);
            throw new RuntimeException("게시물 생성 중 오류 발생", e);
        }
    }
    
    private String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString();
        ObjectId fileId = gridFsTemplate.store(
            file.getInputStream(), filename, file.getContentType()
        );
        return fileId.toString();
    }

    public List<Post> getAllPostsWithUserInfo() {
        try {
            List<Post> posts = postRepository.findAll();
            logger.debug("Retrieved {} posts", posts.size());
            
            for (Post post : posts) {
                if (post.getAuthor() == null) {
                    User user = userRepository.findById(post.getAuthorId())
                            .orElse(new User("알 수 없음"));
                    post.setAuthor(user);
                }
            }
            
            return posts;
        } catch (Exception e) {
            logger.error("Error getting all posts with user info", e);
            throw new RuntimeException("게시물 목록 조회 중 오류 발생", e);
        }
    }

    private Map<String, String> getUserMap(List<Post> posts) {
        List<Long> userIds = posts.stream()
                .map(post -> {
                    try {
                        return post.getAuthorId();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        
        List<User> users = userRepository.findAllById(userIds);
        return users.stream().collect(Collectors.toMap(
            user -> user.getId().toString(), 
            User::getUsername
        ));
    }

    public void deletePost(String postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        if (!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("이 게시물을 삭제할 권한이 없습니다");
        }

        postRepository.delete(post);
        logger.info("게시물 ID {}가 사용자 ID {}에 의해 삭제되었습니다", postId, userId);
    }

    public List<Post> getRecentPosts(int count) {
        return postRepository.findAllByOrderByCreatedAtDesc(org.springframework.data.domain.PageRequest.of(0, count));
    }

    public Post getPostById(String id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && post.getAuthor() == null) {
            User user = userRepository.findById(post.getAuthorId()).orElse(new User("알 수 없음"));
            post.setAuthor(user);
        }
        logger.info("Retrieved post by id {}: {}", id, post);  // 로그 추가
        return post;
    }

    public List<Post> getPostsByUser(Long userId) {
        logger.info("Searching posts for user ID: {}", userId);
        List<Post> posts = postRepository.findByAuthorId(userId);
        logger.info("Found {} posts for user ID: {}", posts.size(), userId);
        for (Post post : posts) {
            logger.debug("Post: id={}, title={}, authorId={}", post.getId(), post.getTitle(), post.getAuthorId());
            if (post.getAuthor() == null) {
                User user = userRepository.findById(userId).orElse(new User("알 수 없음"));
                post.setAuthor(user);
            }
        }
        return posts;
    }

    public void deletePostById(String id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
            postRepository.delete(post);
            logger.info("게시물 ID {}가 삭제되었습니다", id);
        } catch (Exception e) {
            logger.error("게시물 삭제 중 오류 발생. 게시물 ID: {}", id, e);
            throw new RuntimeException("게시물 삭제 중 오류 발생", e);
        }
    }

    public void deleteAllPostsByUser(Long userId) {
        List<Post> userPosts = postRepository.findByAuthorId(userId);
        postRepository.deleteAll(userPosts);
    }
}
