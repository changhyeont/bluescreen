package bluescreensite.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import bluescreensite.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Post> findByAuthorId(Long authorId);
    
    // 또는 쿼리를 명시적으로 정의해 봅니다
    List<Post> findPostsByAuthorId(Long authorId);
}
