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

// 게시판 관련 API 컨트롤러
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BoardController {

    // 게시글 Repository 주입
    private final BoardPostRepository postRepository;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<BoardPostResponse>> getAllPosts() {
        // DB에서 모든 게시글을 조회 후 DTO로 변환
        List<BoardPostResponse> posts = postRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        // ID로 게시글 찾기, 없으면 예외 발생
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return ResponseEntity.ok(toResponseDto(post));
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody BoardPostCreateRequest request) {
        // 요청 DTO를 엔티티로 변환 후 저장
        BoardPost post = new BoardPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setWriter(request.getWriter());
        post.setCreatedAt(LocalDateTime.now()); // 생성 시간 기록

        BoardPost saved = postRepository.save(post);
        return ResponseEntity.ok(toResponseDto(saved));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody BoardPostUpdateRequest request) {
        // 수정할 게시글을 조회, 없으면 예외 발생
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 제목, 내용 업데이트
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postRepository.save(post);

        return ResponseEntity.ok("수정 완료");
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestBody Map<String, String> req) {
        // 삭제할 게시글을 조회, 없으면 예외 발생
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 작성자 본인만 삭제 가능
        if (!post.getWriter().equals(req.get("userId"))) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok("삭제됨");
    }

    // BoardPost 엔티티 BoardPostResponse DTO 변환 메서드
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
