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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final BoardCommentRepository commentRepository;
    private final BoardPostRepository postRepository;

    // 댓글 작성
    @PostMapping("/comments")
    public ResponseEntity<BoardCommentResponse> createComment(@RequestBody BoardCommentCreateRequest request) {
        BoardPost post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        BoardComment comment = new BoardComment();
        comment.setPost(post);
        comment.setContent(request.getContent());
        comment.setWriter(request.getWriter());
        comment.setCreatedAt(LocalDateTime.now());

        BoardComment saved = commentRepository.save(comment);
        return ResponseEntity.ok(toResponse(saved));
    }

    // 댓글 단건 조회
    @GetMapping("/comments/{id}")
    public ResponseEntity<BoardCommentResponse> getComment(@PathVariable Long id) {
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return ResponseEntity.ok(toResponse(comment));
    }

    // 게시글별 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<BoardCommentResponse>> getCommentsByPost(@PathVariable Long postId) {
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
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        comment.setContent(request.getContent());
        commentRepository.save(comment);

        return ResponseEntity.ok("댓글 수정 완료");
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, @RequestBody(required = false) String writer) {
        BoardComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (writer != null && !writer.equals(comment.getWriter())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

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
