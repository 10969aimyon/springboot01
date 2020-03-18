package com.Hello.service;


import com.Hello.dto.PageDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.QuestionModel;
import com.Hello.model.UserModel;
import org.h2.engine.User;
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

}
