package com.Hello.mapper;

import com.Hello.dto.QuestionDto;
import com.Hello.model.QuestionModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {



    @Insert("INSERT INTO question(title,description, creator, tag, gmt_create, gmt_modified) VALUES(#{title}, #{description}, #{creator}, #{tag}, #{gmtCreate}, #{gmtModified});")
    void create(QuestionModel question);

    @Select("SELECT * FROM question LIMIT #{offset} , #{size};")
    List<QuestionModel> list(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT count(1) FROM question;")
    int totalCount();

    @Select("SELECT count(1) FROM question WHERE creator=#{id};")
    int listByIdCount(@Param("id") int id);

    @Select("SELECT * FROM question WHERE creator=#{id} LIMIT #{offset} , #{size};")
    List<QuestionModel> listByID(@Param("id")int id,@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM question WHERE id = #{id};")
    QuestionModel getById(@Param("id") Integer questionId);

    @Update("UPDATE question SET title = #{title}, description = #{description}, tag = #{tag}, gmt_modified = #{gmtModified} WHERE id = #{id};")
    void update(QuestionModel question);
}
