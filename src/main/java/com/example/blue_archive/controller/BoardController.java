package com.example.blue_archive.controller;

import com.example.blue_archive.model.BoardPost;
import com.example.blue_archive.repository.BoardPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class BoardController {

    private final BoardPostRepository postRepository;

    @Autowired
    public BoardController(BoardPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<BoardPost> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody BoardPost post) {
        post.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.ok(postRepository.save(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestBody Map<String, String> req) {
        Optional<BoardPost> optional = postRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        BoardPost post = optional.get();
        if (!post.getWriter().equals(req.get("userId"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok("삭제됨");
    }
}
