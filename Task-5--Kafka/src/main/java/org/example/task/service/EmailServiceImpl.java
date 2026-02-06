package org.example.task.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.example.task.data.Email;
import org.example.task.data.Status;
import org.example.task.dto.EmailDto;
import org.example.task.repository.EmailRepository;

import java.time.Instant;


@Service
public class EmailServiceImpl implements EmailService{

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;

    public EmailServiceImpl(EmailRepository emailRepository, JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    /**
     * Creates a new Email entity from the given EmailDto and saves it to the database.
     *
     * @param emailDto DTO containing email details
     * @return the saved Email entity
     */
    @Override
    public Email createEmail(EmailDto emailDto) {
        // Create a new Email object with status PENDING
        Email email = new Email(
                emailDto.getTo(),
                emailDto.getSubject(),
                emailDto.getContent(),
                emailDto.getSourceService(),
                Status.PENDING,
                Instant.now() // Timestamp for when the email is created
        );
        // Save the email to the repository (database)
        return emailRepository.save(email);
    }

    /**
     * Sends an email using JavaMailSender and updates its status in the database.
     *
     * @param message Email entity to send
     */
    public void sendEmail(Email message) {
        try {
            // Prepare the email message
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(message.getTo());
            mail.setSubject(message.getSubject());
            mail.setText(message.getContent());

            // Send the email
            mailSender.send(mail);

            // Update status to SEND if successful
            message.setStatus(Status.SEND);
            message.setErrorMessage(null);
        } catch (Exception e) {
            // Update status to ERROR and store the error message
            message.setStatus(Status.ERROR);
            message.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        // Save the updated email entity back to the database
        emailRepository.save(message);
    }
}
