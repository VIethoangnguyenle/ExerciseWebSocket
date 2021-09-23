package com.hoang.webSocket.mail;

import com.hoang.webSocket.dto.RestResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("simple")
    public RestResponseDto<Object> sendEmail(@RequestBody MailDto mailDto) {
        emailService.sendHtmlEmail(mailDto);
        return new RestResponseDto<>().success();
    }
}
