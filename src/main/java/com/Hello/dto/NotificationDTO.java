package com.Hello.dto;


import com.Hello.enums.NotificationTypeEnum;
import com.Hello.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private int id;
    private long gmtCreate;
    private NotificationTypeEnum type;
    private int status;
    private User notifier;
    private String outerTitle;
}
