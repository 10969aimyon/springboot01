package com.Hello.mapper;


import com.Hello.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(account_id, name, token, gmt_create, gmt_modified) VALUES(#{accountId}, #{name}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(UserModel user);

    @Select("SELECT * FROM user WHERE token =#{token}")
    UserModel findByToken(@Param("token") String token);
}