package com.Hello.Controller;


import com.Hello.dto.PageDTO;
import com.Hello.mapper.UserMapper;
import com.Hello.model.UserModel;
import com.Hello.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {

    @Value("${perpageCount}")
    private int size;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page){



        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserModel userModel = userMapper.findByToken(token);
                    if (userModel != null) {
                        request.getSession().setAttribute("user", userModel);
                    }
                    break;
                }
            }
        }


        PageDTO currentPage = questionService.list(page, size);
        model.addAttribute("currentPage", currentPage);


        return "index";
    }
}
