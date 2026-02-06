package org.example.task.service;

import org.example.task.data.Email;
import org.example.task.dto.EmailDto;

public interface EmailService {

    Email createEmail(EmailDto emailDto);

    void sendEmail(Email email);

}
