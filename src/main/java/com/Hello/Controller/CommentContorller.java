package com.Hello.controller;

import com.Hello.dto.CommentDTO;
import com.Hello.dto.CommentListDTO;
import com.Hello.dto.ResultDTO;
import com.Hello.enums.CommentTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.model.Comment;
import com.Hello.model.CommentExample;
import com.Hello.model.User;
import com.Hello.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentContorller {

    @Autowired
    private CommentService commentService;

    // 发布一级评论
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentDTO.getContent().equals("") || commentDTO.getContent() == null ){
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    // 查看二级评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id") int id){

        List<CommentListDTO> commentListDTOS =  commentService.getCommentListDTOListById(id, CommentTypeEnum.COMMENT);

        return ResultDTO.okOf(commentListDTOS);
    }

    // 发布二级评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.POST)
    public Object publish_second_comment(@RequestBody Comment requestComment,
                                       @PathVariable(name = "id") int id,
                                       HttpServletRequest request){
        // 首先看看有没有登陆，顺便拿到user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if (requestComment.getContent().equals("") || requestComment == null){
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        // 新建一个往数据库中插入的对象
        Comment comment = new Comment();
        comment.setCommentator(user.getId());
        comment.setContent(requestComment.getContent());
        comment.setType(CommentTypeEnum.COMMENT.getType());
        comment.setParentId(id);
        comment.setLikeCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        commentService.insert(comment);
        return ResultDTO.okOf();

    }

}
