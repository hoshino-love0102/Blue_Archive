package com.example.blue_archive.dto.boardcomment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCommentCreateRequest {

    private Long postId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "작성자는 필수입니다.")
    private String writer;
}
