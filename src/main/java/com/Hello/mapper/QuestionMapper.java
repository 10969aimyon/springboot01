package com.Hello.mapper;

import com.Hello.model.QuestionModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO question(title,description, creator, tag, gmt_create, gmt_modified) VALUES(#{title}, #{description}, #{creator}, #{tag}, #{gmtCreate}, #{gmtModified});")
    void create(QuestionModel question);

    @Select("Select * FROM question")
    List<QuestionModel> list();
}
