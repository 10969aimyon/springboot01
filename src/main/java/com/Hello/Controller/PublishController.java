package com.Hello.Controller;


import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.QuestionModel;
import com.Hello.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String toPublish(
            @RequestParam("title") String title,
            @RequestParam("tag") String tag,
            @RequestParam("description") String description,
            HttpServletRequest request,
            Model model){

        UserModel userModel = new UserModel();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                userModel = userMapper.findByToken(token);
                if (userModel != null){
                    request.getSession().setAttribute("user", userModel);
                }
                break;
            }
        }
        if (userModel == null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }

        QuestionModel question = new QuestionModel();
        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(userModel.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        questionMapper.create(question);
        return "redirect:/";
    }
}
