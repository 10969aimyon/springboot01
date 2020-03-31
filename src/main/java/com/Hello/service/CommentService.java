package com.Hello.service;


import com.Hello.dto.CommentListDTO;
import com.Hello.enums.CommentTypeEnum;
import com.Hello.enums.NotificationStatusEnum;
import com.Hello.enums.NotificationTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.mapper.*;
import com.Hello.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    // 此注解将方法变成事务，如果后面有抛异常，前面的操作不会执行
    @Transactional
    public void insert(Comment comment) {
        // 判断parentId是否存在
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        }
        // 判断type是否存在，如果存在判断是否属于1或2
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        // 如果type类型为对评论的回复（2）
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            // 回复评论
            // 这里之所以传入的是parentId是因为对评论回复的parentId就是另一个对问题回复的comment的id
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            //如果通过这个parentId没找到comment（比如在回复的过程中别人删了评论）
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            // 创建通知对象插入到数据库
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);

        }else{
            // 如果type类型为对问题下的评论（1）
            // 回复问题(需要增加评论数)
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            // 跟上面相似，如果在评论的过程中对方删了问题，就抛出异常
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            // 这里使用了Ext自定义的Mapper，以应对并发操作
            questionExtMapper.incComment(question);

            // 通知
            createNotify(comment, question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
        }
    }

    // 创建通知
    private void createNotify(Comment comment, int reciver, NotificationTypeEnum n) {
        // 如果通知的创建者和接受者一样，就不创建通知
        if (comment.getCommentator() == reciver){
            return;
        }
        // 添加notification对象
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(n.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(reciver);
        notificationMapper.insert(notification);
    }

    public List<CommentListDTO> getCommentListDTOListById(Integer id, CommentTypeEnum type) {
        // 首先找到所有的主键id为id的并且type为type的comment
        // 然后找到每个comment对应的user

        // 创建一个要返回的List
        List<CommentListDTO> commentListDTOS = new ArrayList();

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        // 评论按照时间排序，新的在最前面
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        for (Comment comment : commentList) {
            // 找到comment对应的creator
            User user = new User();
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdEqualTo(comment.getCommentator());
            user = userMapper.selectByExample(userExample).get(0);

            // 找到每条评论下面的子评论数
            CommentExample example = new CommentExample();
            example.createCriteria()
                    .andParentIdEqualTo(comment.getId())
                    .andTypeEqualTo(CommentTypeEnum.COMMENT.getType());
            int count = (int)commentMapper.countByExample(example);
            comment.setCount(count);
            commentListDTOS.add(new CommentListDTO(comment, user));
        }
        return commentListDTOS;
    }
}
