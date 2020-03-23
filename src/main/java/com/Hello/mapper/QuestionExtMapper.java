package com.Hello.mapper;

import com.Hello.model.Question;

public interface QuestionExtMapper {
    int incView(Question recode);
    int incComment(Question record);
}