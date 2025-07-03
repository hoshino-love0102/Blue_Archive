package com.example.blue_archive.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardPostResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}

