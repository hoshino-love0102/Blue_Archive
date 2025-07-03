package com.example.blue_archive.dto.boardcomment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCommentResponse {
    private Long id;
    private Long postId;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}
