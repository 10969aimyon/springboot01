package com.Hello.service;

import com.Hello.dto.NotificationDTO;
import com.Hello.dto.PageDTO;
import com.Hello.enums.NotificationStatusEnum;
import com.Hello.enums.NotificationTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.mapper.CommentMapper;
import com.Hello.mapper.NotificationMapper;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    /** id:当前登陆的人的id
     *  这里要返回一个PageDTO,里面包括了：
     *  通知列表 List<NotificationDTO>
     *  页码：
     *      是否显示上一页、下一页、第一页、最后一页
     *      显示的页面列表 list<int> 、页面总数、当前页面页码数
     */

    public PageDTO<NotificationDTO> list(Integer id, int page, int size) {
        // 查找数量总数totalCount
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        notificationExample.setOrderByClause("status, gmt_create desc");
        int totalCount = (int) notificationMapper.countByExample(notificationExample);

        // 创建要返回的对象
        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        // 通过传过来的page确定了大部分值
        pageDTO.setPage(page, size, totalCount);

        // 校验page
        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        // 确定offset
        int offset = size * (page - 1);

        // 确定当前页面要返回的通知列表list
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        // 创建可以往pageDTP里面添加集合
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        // 转换
        for (Notification notification : notifications) {
            User user = userMapper.selectByPrimaryKey(notification.getNotifier());
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotifier(user);
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setGmtCreate(notification.getGmtCreate());
            notificationDTO.setId(notification.getId());
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()){
                notificationDTO.setType(NotificationTypeEnum.REPLY_QUESTION);
                int questionId = notification.getOuterId();
                Question question = questionMapper.selectByPrimaryKey(questionId);
                notificationDTO.setOuterTitle(question.getTitle());
            } else if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()){
                notificationDTO.setType(NotificationTypeEnum.REPLY_COMMENT);
                int commentId = notification.getOuterId();
                Comment comment = commentMapper.selectByPrimaryKey(commentId);
                notificationDTO.setOuterTitle(comment.getContent());
            }
            notificationDTOS.add(notificationDTO);
        }

        pageDTO.setData(notificationDTOS);
        return pageDTO;
    }

    public int getUnreadCount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(user.getId())
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        int unreadCount = (int) notificationMapper.countByExample(notificationExample);
        return unreadCount;
    }
}
