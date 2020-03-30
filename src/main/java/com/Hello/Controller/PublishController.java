package com.Hello.controller;


import com.Hello.cache.tagCache;
import com.Hello.dto.QuestionDto;
import com.Hello.model.Question;
import com.Hello.model.User;
import com.Hello.service.QuestionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish/{id}")
    public String edit(@PathVariable int id,
                       Model model){
        QuestionDto question = questionService.getByQuestionID(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", tagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", tagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "id", required = false) Integer id,
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

        String invalid = tagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}



