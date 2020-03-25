package com.Hello.advice;


import com.Hello.dto.ResultDTO;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    // 所有的exception这里都接收
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        String contentType = request.getContentType();
        // 如果请求的contentType为：application/json 就代表这是一个api请求
        // 需要返回json而不是页面
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            //如果这个异常属于我们自定义的异常
            if (e instanceof CustomizeException) {
                // 将异常传入resultDTO对象，返回对象作为json
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                //如果不属于我们自定义的异常，就返回一个通用的系统错误
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            // 自定义返回api接口的值

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                // 此处可理解为将resultDTO改写response对象的json并返回
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            // 如果不是api请求
            // 就返回页面
            if (e instanceof CustomizeException) {
                // 如果恰好属于我们自定义的异常，就将相应异常的异常信息打印到前端页面
                model.addAttribute("message", e.getMessage());
            } else {
                // 如果不是我们自定义的异常，就返回前端系统错误
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            // 返回前端页面
            return new ModelAndView("error");
        }
    }
}
