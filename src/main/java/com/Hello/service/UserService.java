package com.Hello.service;


import com.Hello.mapper.UserMapper;
import com.Hello.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(UserModel userModel) {
        UserModel user = userMapper.findByAccountId(userModel.getAccountId());
        if (user != null){
            // 更新
            userModel.setGmtModified(System.currentTimeMillis());
            userMapper.update(userModel);

        }else{
            // 插入
            userModel.setGmtCreate(System.currentTimeMillis());
            userModel.setGmtModified(userModel.getGmtCreate());
            userMapper.insert(userModel);
        }
    }
}
