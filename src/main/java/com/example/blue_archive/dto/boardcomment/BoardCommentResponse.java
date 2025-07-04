package com.example.blue_archive.dto.boardcomment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 게시판 댓글 응답 DTO
@Getter
@Setter
public class BoardCommentResponse {

    // 댓글 ID
    private Long id;

    // 댓글이 달린 게시글 ID
    private Long postId;

    // 댓글 내용
    private String content;

    // 댓글 작성자
    private String writer;

    // 댓글 작성일시
    private LocalDateTime createdAt;
}
