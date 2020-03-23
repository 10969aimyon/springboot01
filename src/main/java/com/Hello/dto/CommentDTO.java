package com.Hello.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer parentId;
    private String content;
    private int type;
}
