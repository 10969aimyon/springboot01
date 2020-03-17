package com.Hello.model;

import lombok.Data;

@Data
public class QuestionModel {
    private Integer id;
    private String title;
    private String description;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private String tag;
    private long gmtCreate;
    private long gmtModified;

}
