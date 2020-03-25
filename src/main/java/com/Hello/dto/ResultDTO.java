package com.Hello.dto;

import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import lombok.Data;
import org.h2.api.ErrorCode;

@Data
public class ResultDTO<T> {
    private String message;
    private int code;
    private T data;



    // 接受一个code，message来构造一个返回json的对象
    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    // 将异常枚举对象的code和message调用上一个方法，最终返回json
    // 处理异常时才用到
    public static ResultDTO errorOf(CustomizeErrorCode errorCode){
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }
    // 将异常的code和message调用上上个方法，最终返回json

    /**
     * 为什么定义一个 errorcode枚举类 又定义一个 exception类呢，他们里面都有code和message
     * 因为某些时候不是api请求，我们需要返回一个自定义的错误页面到前端
     * 这个时候我们就只可以抛出一个异常，但是异常构造方法里面可以接受一个errorcode枚举类
     * 通过自定义的errorcode，我们就可以将它的code和message返回到exception中，
     * 最终仍然是一个完整的exception被抛出了放到advice里去接收
     *
     * 而如果是需要返回一个json错误，我们就不能直接抛出异常
     * 而是定义这个类ResultDTO，它里面可以接收枚举类自定义的对象，通过这些静态方法
     * 接收到这些对象的code和message，做成json返回
     * */

    // 当需要返回json时，
    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    // 返回成功时候的json
    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    //
    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
