package com.hrms.modules.broadcast.service;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IEmailService {
    void sendAttnMail(Long id) throws MessagingException, IOException;
}
