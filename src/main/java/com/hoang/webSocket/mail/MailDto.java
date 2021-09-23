package com.hoang.webSocket.mail;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Accessors(chain = true)
public class MailDto {
    private String mailTo;
    private String message;
    private String subject;
}
