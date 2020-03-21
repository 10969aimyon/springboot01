package com.Hello.mapper;


import com.Hello.model.UserModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;


@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE token =#{token}")
    UserModel findByToken(@Param("token") String token);

    @Select("SELECT * FROM user WHERE id = #{id}")
    UserModel findById(@Param("id")Integer id);

    @Select("SELECT * FROM user WHERE account_id = #{accountId};")
    UserModel findByAccountId(@Param("accountId") String accountId);

    @Select("INSERT INTO user(name, account_id, token, gmt_create, gmt_modified, avatar_url) VALUES(#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl});")
    void insert(UserModel user);

    @Update("UPDATE user SET name = #{name}, token = #{token}, gmt_modified=#{gmtModified}, avatar_url=#{avatarUrl} WHERE account_id=#{accountId}")
    void update(UserModel userModel);
}
