package com.Hello.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001, "你寻找的问题不存在，请换一个试试。"),
    TARGET_NOT_FOUND(2002,"未选中任何评论或问题进行回复"),
    NO_LOGIN(2003,"当前操作需要登陆后在操作"),
    SYS_ERROR(2004, "服务出现错误，请联系管理员"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007,"回复内容为空")
    ;
    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
