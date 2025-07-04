package com.example.blue_archive.controller;

import com.example.blue_archive.dto.boardpost.BoardPostCreateRequest;
import com.example.blue_archive.dto.boardpost.BoardPostResponse;
import com.example.blue_archive.dto.boardpost.BoardPostUpdateRequest;
import com.example.blue_archive.exception.CustomException;
import com.example.blue_archive.exception.ErrorCode;
import com.example.blue_archive.model.BoardPost;
import com.example.blue_archive.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BoardController {

    private final BoardPostRepository postRepository;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<BoardPostResponse>> getAllPosts() {
        List<BoardPostResponse> posts = postRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return ResponseEntity.ok(toResponseDto(post));
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody BoardPostCreateRequest request) {
        BoardPost post = new BoardPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setWriter(request.getWriter());
        post.setCreatedAt(LocalDateTime.now());

        BoardPost saved = postRepository.save(post);
        return ResponseEntity.ok(toResponseDto(saved));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody BoardPostUpdateRequest request) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postRepository.save(post);

        return ResponseEntity.ok("수정 완료");
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestBody Map<String, String> req) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (!post.getWriter().equals(req.get("userId"))) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok("삭제됨");
    }

    // Entity → Response DTO 변환 메서드
    private BoardPostResponse toResponseDto(BoardPost post) {
        BoardPostResponse dto = new BoardPostResponse();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setWriter(post.getWriter());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}
