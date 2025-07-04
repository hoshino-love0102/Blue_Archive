package com.example.blue_archive.dto.boardpost;

import lombok.Getter;
import lombok.Setter;

// 게시판 글 수정 요청 DTO
@Getter
@Setter
public class BoardPostUpdateRequest {

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;
}
