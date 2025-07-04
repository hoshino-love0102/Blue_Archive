package com.example.blue_archive.dto.boardpost;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시판 글 응답 DTO
@Getter
@Setter
public class BoardPostResponse {

    // 게시글 ID
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 게시글 작성자
    private String writer;

    // 게시글 작성일시
    private LocalDateTime createdAt;
}
