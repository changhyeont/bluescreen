package com.example.bluescreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

import com.example.bluescreen.model.Board;
import com.example.bluescreen.repository.BoardRepository;
import com.example.bluescreen.model.User;
import com.example.bluescreen.repository.UserRepository;
import com.example.bluescreen.dto.BoardRequest;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoard(String id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));
    }

    public Board createBoard(BoardRequest request, MultipartFile image, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = fileService.saveFile(image);
        }

        Board board = Board.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .imageUrl(imageUrl)
            .userId(user.getId())
            .username(user.getUsername())
            .author(user.getUsername())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return boardRepository.save(board);
    }

    public Board updateBoard(String id, BoardRequest request, MultipartFile image, String username) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!board.getAuthor().equals(username)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다");
        }

        String imageUrl = board.getImageUrl();
        if (image != null && !image.isEmpty()) {
            if (imageUrl != null) {
                fileService.deleteFile(imageUrl);
            }
            imageUrl = fileService.saveFile(image);
        }

        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setImageUrl(imageUrl);
        board.setUpdatedAt(LocalDateTime.now());

        return boardRepository.save(board);
    }

    public void deleteBoard(String id, String username) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        if (!board.getAuthor().equals(username)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다");
        }

        boardRepository.deleteById(id);
    }

    public List<Board> getBoardsByUsername(String username) {
        return boardRepository.findByUsername(username);
    }
}
