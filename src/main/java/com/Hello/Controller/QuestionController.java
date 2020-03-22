package com.Hello.Controller;

import com.Hello.dto.QuestionDto;
import com.Hello.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions/{questionId}")
    public String question(@PathVariable Integer questionId,
                           Model model){

        QuestionDto questionDto = questionService.getByQuestionID(questionId);
        // 累加阅读数
        questionService.incView(questionId);
        model.addAttribute("question",questionDto);
        return "question";
    }
}

