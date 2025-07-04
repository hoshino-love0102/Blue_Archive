package com.example.blue_archive.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시글 엔티티
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardPost {

    // 게시글 ID (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 게시글 작성자
    private String writer;

    // 게시글 작성 일시
    private LocalDateTime createdAt;
}
