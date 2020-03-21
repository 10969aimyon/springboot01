package com.Hello.service;


import com.Hello.dto.PageDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.QuestionModel;
import com.Hello.model.UserModel;
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
    private UserMapper userMapper;


    public PageDTO list(int page, int size) {
        int totalCount = questionMapper.totalCount();
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = size * (page - 1);

        List<QuestionModel> questionModelList = questionMapper.list(offset, size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (QuestionModel questionModel : questionModelList) {
            UserModel user = userMapper.findById(questionModel.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(questionModel, questionDto);
            questionDto.setUserModel(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setQuesetions(questionDtoList);



        return pageDTO;
    }

    public PageDTO list(Integer id, Integer page, Integer size){
        int totalCount = questionMapper.listByIdCount(id);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = size * (page - 1);

        List<QuestionModel> questionModelList = questionMapper.listByID(id, offset, size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (QuestionModel questionModel : questionModelList) {
            UserModel user = userMapper.findById(questionModel.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(questionModel, questionDto);
            questionDto.setUserModel(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setQuesetions(questionDtoList);

        return pageDTO;

    }

    public QuestionDto getByQuestionID(Integer questionId) {
        QuestionModel questionModel = questionMapper.getById(questionId);
        UserModel userModel = new UserModel();
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(questionModel, questionDto);
        userModel = userMapper.findById(questionModel.getCreator());
        questionDto.setUserModel(userModel);

        return questionDto;
    }

    public void createOrUpdate(QuestionModel question) {
        if (question.getId() != null){
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }else {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }
    }
}
