package com.example.blue_archive.dto.boardcomment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 게시판 댓글 수정 요청 DTO
@Getter
@Setter
public class BoardCommentUpdateRequest {

    // 댓글 내용 (필수)
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
