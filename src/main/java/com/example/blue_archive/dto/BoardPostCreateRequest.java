package com.example.blue_archive.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostCreateRequest {
    private String title;
    private String content;
    private String writer;
}