package com.example.blue_archive.controller;

import com.example.blue_archive.model.BoardComment;
import com.example.blue_archive.repository.BoardCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final BoardCommentRepository commentRepository;

    @Autowired
    public CommentController(BoardCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody BoardComment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    // 특정 게시글의 댓글 목록
    @GetMapping("/{postId}")
    public List<BoardComment> getComments(@PathVariable Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @RequestBody(required = false) String writer) {
        Optional<BoardComment> optional = commentRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        BoardComment comment = optional.get();

        // 작성자 확인 (추후 로그인 연동 시 개선 가능)
        if (writer != null && !writer.equals(comment.getWriter())) {
            return ResponseEntity.status(403).body("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(id);
        return ResponseEntity.ok("댓글 삭제됨");
    }
}
