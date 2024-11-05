package com.example.bluescreen.repository;

import com.example.bluescreen.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    List<Board> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    List<Board> findByUsername(String username);
    List<Board> findAllByOrderByCreatedAtDesc();
}
