package com.example.blue_archive.controller;

import com.example.blue_archive.model.BoardPost;
import com.example.blue_archive.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BoardController {

    private final BoardPostRepository postRepository;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<BoardPost>> getAllPosts() {
        List<BoardPost> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return ResponseEntity.ok(post);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody BoardPost post) {
        post.setCreatedAt(LocalDateTime.now());
        BoardPost saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody BoardPost updatedPost) {
        BoardPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postRepository.save(post);

        return ResponseEntity.ok("수정 완료");
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestBody Map<String, String> req) {
        Optional<BoardPost> optional = postRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BoardPost post = optional.get();

        // 작성자 체크
        if (!post.getWriter().equals(req.get("userId"))) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("작성자만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok("삭제됨");
    }
}
