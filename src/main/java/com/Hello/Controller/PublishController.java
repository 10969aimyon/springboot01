package com.Hello.Controller;


import com.Hello.mapper.QuestionMapper;
import com.Hello.model.QuestionModel;
import com.Hello.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

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
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title == null || title.equals("")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")){
            model.addAttribute("error","描述不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")){
            model.addAttribute("error","tag不能为空");
            return "publish";
        }

        UserModel userModel = (UserModel) request.getSession().getAttribute("user");
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
