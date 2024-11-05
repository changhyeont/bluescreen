package com.example.bluescreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.bluescreen.service.BoardService;
import com.example.bluescreen.model.Board;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.example.bluescreen.dto.BoardRequest;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "게시판 API", description = "게시판 CRUD API")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "전체 게시글 조회")
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{id}")
    public Board getBoard(@PathVariable String id) {
        return boardService.getBoard(id);
    }

    @Operation(summary = "게시글 작성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Board> createBoard(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        
        BoardRequest request = BoardRequest.builder()
            .title(title)
            .content(content)
            .build();
            
        Board board = boardService.createBoard(request, image, userDetails.getUsername());
        return ResponseEntity.ok(board);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Board> updateBoard(
            @PathVariable String id,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        BoardRequest request = BoardRequest.builder()
            .title(title)
            .content(content)
            .build();
            
        Board board = boardService.updateBoard(id, request, image, userDetails.getUsername());
        return ResponseEntity.ok(board);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteBoard(@PathVariable String id,
                          @AuthenticationPrincipal UserDetails userDetails) {
        boardService.deleteBoard(id, userDetails.getUsername());
    }
}
