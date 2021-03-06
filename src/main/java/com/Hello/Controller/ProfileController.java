package com.Hello.controller;

import com.Hello.dto.CountDTO;
import com.Hello.dto.NotificationDTO;
import com.Hello.dto.PageDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.model.User;
import com.Hello.service.NotificationService;
import com.Hello.service.QuestionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @Value("${perPageCount}")
    private int size;


    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") int page){

       User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }


        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PageDTO<QuestionDto> pageDto = questionService.list(user.getId(), page, size);
            model.addAttribute("pageDTO", pageDto);
        }else if ("replies".equals(action)){

            PageDTO<NotificationDTO> pageDto = notificationService.list(user.getId(), page, size);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("pageDTO", pageDto);

        }

        // 未读消息数量
        return "profile";
    }
}
