package org.example.task;

import org.example.task.data.Email;
import org.example.task.data.Status;
import org.example.task.repository.EmailRepository;
import org.example.task.service.EmailRetrySchedulerServiceImpl;
import org.example.task.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


@SpringBootTest(properties = {
        // Exclude the real MailSender auto-configuration to use a mock
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration"})
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailRetrySchedulerServiceImpl emailRetryScheduler;

    @MockitoBean
    private JavaMailSender mailSender;

    @BeforeEach
    void cleanUp() {
        emailRepository.deleteAll();
    }

    /**
     * Test sending an email successfully.
     * Ensures that the email status is updated to SEND and there is no error message.
     */
    @Test
    void testSendEmailSuccess(){
        Email email = new Email(
                "test@example.com",
                "Subject",
                "Content",
                "Service",
                Status.PENDING ,
                Instant.now());

        // Mock the mailSender to do nothing when sending an email
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(email);

        Email savedEmail = emailRepository.findById(email.getId()).orElseThrow();
        assertEquals(Status.SEND, savedEmail.getStatus());
        assertNull(savedEmail.getErrorMessage());
    }

    /**
     * Test that the retry scheduler retries failed emails.
     * Verifies that the email status is updated, retry count is incremented, and lastAttempt is set.
     */
    @Test
    void testSchedulerRetriesFailedEmails() {
        Email failedEmail = new Email(
                "retry@test.com", "Subject", "Body", "Service",
                Status.ERROR, Instant.now()
        );
        failedEmail.setRetryCount(0);
        emailRepository.save(failedEmail);

        // Mock the mailSender to do nothing
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Run the retry scheduler
        emailRetryScheduler.retryFailedEmails();

        Email updated = emailRepository.findById(failedEmail.getId()).get();
        assertEquals(Status.SEND, updated.getStatus());
        assertEquals(1, updated.getRetryCount());
        assertNotNull(updated.getLastAttempt());
    }


}
