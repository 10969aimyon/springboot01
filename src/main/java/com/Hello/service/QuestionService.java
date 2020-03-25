package com.Hello.service;


import com.Hello.dto.PageDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.enums.CommentTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.mapper.CommentMapper;
import com.Hello.mapper.QuestionExtMapper;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.CommentExample;
import com.Hello.model.Question;
import com.Hello.model.QuestionExample;
import com.Hello.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    // 可以同时使用QuestionMapper和UserMapper来组装

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;


    public PageDTO list(int page, int size) {
        int totalCount = (int)questionMapper.countByExample(new QuestionExample());

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = size * (page - 1);


        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setQuesetions(questionDtoList);



        return pageDTO;
    }

    public PageDTO list(Integer id, Integer page, Integer size){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        int totalCount = (int) questionMapper.countByExample(questionExample);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(id);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setQuesetions(questionDtoList);

        return pageDTO;

    }

    public QuestionDto getByQuestionID(Integer questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = new User();
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);

        // 查找评论数
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        int commentCount = (int)commentMapper.countByExample(commentExample);
        questionDto.setCommentCount(commentCount);

        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() != null){
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }else {
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }
    }

    public void incView(Integer questionId) {
        Question question = new Question();
        question.setId(questionId);
        questionExtMapper.incView(question);
    }
}
