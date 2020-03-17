package com.Hello.dto;


import com.Hello.model.UserModel;
import lombok.Data;

@Data
public class QuestionDto {
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
    private UserModel userModel;
}
