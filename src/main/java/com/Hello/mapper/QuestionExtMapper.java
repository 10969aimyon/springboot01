package com.Hello.mapper;

import com.Hello.dto.QuestionQueryDTO;
import com.Hello.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question recode);

    int incComment(Question record);

    List<Question> selectRelated(Question question);

    int countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}