package com.Hello.dto;


import com.Hello.model.Comment;
import com.Hello.model.User;
import lombok.Data;


@Data
public class CommentListDTO {
    Comment comment;
    User user;



    public CommentListDTO(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }


}
