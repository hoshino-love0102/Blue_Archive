package com.example.blue_archive.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 댓글 엔티티
@Entity
@Getter
@Setter
public class BoardComment {

    // 댓글 ID (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관된 게시글 (N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BoardPost post;

    // 댓글 내용
    private String content;

    // 댓글 작성자
    private String writer;

    // 댓글 작성 일시
    private LocalDateTime createdAt;
}
