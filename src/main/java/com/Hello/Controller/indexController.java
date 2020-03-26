package com.Hello.controller;


import com.Hello.dto.PageDTO;
import com.Hello.model.Question;
import com.Hello.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {

    @Value("${perPageCount}")
    private int size;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page){

        PageDTO currentPage = questionService.list(page, size);
        model.addAttribute("currentPage", currentPage);
        // 热门问题
        List<Question> questions = questionService.selectMostPop();
        model.addAttribute("mostPopQuestion",questions);
        return "index";
    }
}
