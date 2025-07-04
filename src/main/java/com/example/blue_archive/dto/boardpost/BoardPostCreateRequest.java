package com.example.blue_archive.dto.boardpost;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 게시판 글 생성 요청 DTO
@Getter
@Setter
public class BoardPostCreateRequest {

    // 게시글 제목 (필수)
    @NotBlank(message = "제목은 필수로 들어가야 합니다.")
    private String title;

    // 게시글 내용 (필수)
    @NotBlank(message = "내용은 필수로 들어가야 합니다.")
    private String content;

    // 게시글 작성자 (필수)
    @NotBlank(message = "작성자는 필수로 들어가야 합니다.")
    private String writer;
}
