package com.Hello.controller;


import com.Hello.enums.NotificationStatusEnum;
import com.Hello.enums.NotificationTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.mapper.CommentMapper;
import com.Hello.mapper.NotificationMapper;
import com.Hello.mapper.QuestionMapper;
import com.Hello.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotificationController {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") int notifyId,
                               Model model,
                               HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登陆");
            return "index";
        }

        Notification notification = notificationMapper.selectByPrimaryKey(notifyId);
        // 判断该通知是不是自己的
        if (notification.getReceiver() != user.getId()){
            throw new CustomizeException(CustomizeErrorCode.WRONG_USER);
        }

        // 如果是对问题的回复
        if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()){
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationMapper.updateByPrimaryKey(notification);
            String redirectUrl = "redirect:/questions/"+notification.getOuterId();
            return redirectUrl;
        } else if (notification.getType()==NotificationTypeEnum.REPLY_COMMENT.getType()){
            // 如果是对评论的回复，这里无法返回到评论页面，只能先返回问题页面
            Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterId());
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationMapper.updateByPrimaryKey(notification);
            String redirectUrl = "redirect:/questions/"+comment.getParentId();
            return redirectUrl;
        }
        return null;
    }
}




