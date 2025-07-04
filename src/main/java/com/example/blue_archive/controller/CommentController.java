package com.example.blue_archive.controller;

import com.example.blue_archive.dto.boardcomment.BoardCommentCreateRequest;
import com.example.blue_archive.dto.boardcomment.BoardCommentResponse;
import com.example.blue_archive.dto.boardcomment.BoardCommentUpdateRequest;
import com.example.blue_archive.exception.CustomException;
import com.example.blue_archive.exception.ErrorCode;
import com.example.blue_archive.model.BoardComment;
import com.example.blue_archive.model.BoardPost;
import com.example.blue_archive.repository.BoardCommentRepository;
import com.example.blue_archive.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 댓글 관련 API 컨트롤러
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    // 댓글 레포지토리
    private final BoardCommentRepository commentRepository;

    // 게시글 레포지토리
    private final BoardPostRepository postRepository;

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<BoardCommentResponse> createComment(@RequestBody BoardCommentCreateRequest request) {
        // 댓글 달 게시글 존재 여부 확인
        BoardPost post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 댓글 엔티티 생성 및 값 세팅
        BoardComment comment = new BoardComment();
        comment.setPost(post);
        comment.setContent(request.getContent());
        comment.setWriter(request.getWriter());
        comment.setCreatedAt(LocalDateTime.now());

        // 저장 후 DTO로 변환해서 반환
        BoardComment saved = commentRepository.save(comment);
        return ResponseEntity.ok(toResponse(saved));
    }

    // 댓글 단건 조회
    @GetMapping("/comments/{id}")
    public ResponseEntity<BoardCommentResponse> getComment(@PathVariable Long id) {
        // 댓글 ID로 조회, 없으면 예외
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return ResponseEntity.ok(toResponse(comment));
    }

    // 게시글별 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<BoardCommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        // 특정 게시글에 달린 모든 댓글 조회
        List<BoardComment> comments = commentRepository.findByPostId(postId);
        List<BoardCommentResponse> responses = comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // 댓글 수정
    @PutMapping("/comments/{id}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestBody BoardCommentUpdateRequest request) {
        // 수정할 댓글 조회
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 내용 업데이트
        comment.setContent(request.getContent());
        commentRepository.save(comment);

        return ResponseEntity.ok("댓글 수정 완료");
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestBody(required = false) String writer) {
        // 삭제할 댓글 조회
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 작성자 본인인지 확인 (writer가 넘어온 경우)
        if (writer != null && !writer.equals(comment.getWriter())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    // BoardComment 엔티티 → BoardCommentResponse DTO 변환 메서드
    private BoardCommentResponse toResponse(BoardComment comment) {
        BoardCommentResponse dto = new BoardCommentResponse();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setContent(comment.getContent());
        dto.setWriter(comment.getWriter());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
