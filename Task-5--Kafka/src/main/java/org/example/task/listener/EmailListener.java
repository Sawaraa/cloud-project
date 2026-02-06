package org.example.task.listener;

import lombok.RequiredArgsConstructor;
import org.example.task.data.Email;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.example.task.dto.EmailDto;
import org.example.task.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailListener {

    private final EmailService emailService;

    /**
     * Kafka listener for incoming EmailDto messages.
     * Listens to the topic defined in application properties and processes emails.
     *
     * @param emailDto the incoming email data transfer object
     */
    @KafkaListener(topics = "${kafka.topic.email}", groupId = "email-service-group")
    public void listen(EmailDto emailDto) {
        // Create Email entity from DTO and save it
        Email email = emailService.createEmail(emailDto);
        // Send the email
        emailService.sendEmail(email);
    }

}
