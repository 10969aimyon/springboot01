package com.Hello.dto;


import lombok.Data;

@Data
public class CountDTO {
    int questionCount;
    int unreadNotCount;

    public CountDTO(int questionCount, int unreadNotCount) {
        this.questionCount = questionCount;
        this.unreadNotCount = unreadNotCount;
    }

}
