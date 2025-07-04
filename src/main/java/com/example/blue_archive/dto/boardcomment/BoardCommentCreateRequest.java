package com.example.blue_archive.dto.boardcomment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 게시판 댓글 생성 요청 DTO
@Getter
@Setter
public class BoardCommentCreateRequest {

    //댓글이 달릴 게시글 ID
    private Long postId;

    // 댓글 내용 (필수)
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    // 댓글 작성자 (필수)
    @NotBlank(message = "작성자는 필수입니다.")
    private String writer;
}
