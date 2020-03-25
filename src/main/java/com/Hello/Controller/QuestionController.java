package com.Hello.controller;

import com.Hello.dto.CommentListDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.enums.CommentTypeEnum;
import com.Hello.service.CommentService;
import com.Hello.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;


@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/questions/{questionId}")
    public String question(@PathVariable Integer questionId,
                           Model model){

        QuestionDto questionDto = questionService.getByQuestionID(questionId);
        // 累加阅读数
        questionService.incView(questionId);
        // 返回评论列表
        List<CommentListDTO> commentListDTOS = new ArrayList<>();
        commentListDTOS = commentService.getCommentListDTOListById(questionId, CommentTypeEnum.QUESTION);
        model.addAttribute("question",questionDto);
        model.addAttribute("commentListDTOS",commentListDTOS);
        return "question";
    }
}

