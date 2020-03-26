package com.Hello.mapper;

import com.Hello.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question recode);

    int incComment(Question record);

    List<Question> selectRelated(Question question);
}