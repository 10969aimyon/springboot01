package com.Hello.interceptor;

import com.Hello.mapper.UserMapper;
import com.Hello.model.User;
import com.Hello.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


// 登陆逻辑
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求的时候接收到客户端中的cookie
        Cookie[] cookies = request.getCookies();
        // 创建一个user
        User user;
        // 判断cookie中又没有东西
        if (cookies != null && cookies.length != 0) {
            // cookie中有东西，遍历
            for (Cookie cookie : cookies) {
                // 如果在其中找到了token
                if (cookie.getName().equals("token")) {
                    // 获取到这个token
                    String token = cookie.getValue();
                    // 放到数据库中进行比较，看看有没有这样的user
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    // 如果有这样的user，那就setSession吧
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                    }
                    break;
                }
            }
        }
        // 这里返回true才可以进行下边的步骤
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
