package com.Hello.Controller;


import com.Hello.dto.QuestionDto;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.QuestionModel;
import com.Hello.model.UserModel;
import com.Hello.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class indexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
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

        // 查询发布的话题列表
        // 此时因为question表中并没有头像列表，所以需要通过id找到对应的avatarUrl

        List<QuestionDto> questionDtoList = questionService.list();
        model.addAttribute("questions", questionDtoList);


        return "index";
    }
}
