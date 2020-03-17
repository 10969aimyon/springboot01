package com.Hello.model;


import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private long gmtCreate;
    private long gmtModified;
    private String avatarUrl;

}
