package com.Hello.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private long id;
    private String bio;
    private String avatar_url;

    @Override
    public String toString() {
        return "GithubUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                '}';
    }


}
