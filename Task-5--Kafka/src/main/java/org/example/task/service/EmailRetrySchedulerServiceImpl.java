package org.example.task.service;

import org.example.task.data.Email;
import org.example.task.data.Status;
import org.example.task.repository.EmailRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EmailRetrySchedulerServiceImpl {

    private final EmailRepository emailRepository;
    private final EmailService emailService;

    public EmailRetrySchedulerServiceImpl(EmailRepository emailRepository, EmailService emailService) {
        this.emailRepository = emailRepository;
        this.emailService = emailService;
    }

    /**
     * Scheduled task that retries sending failed emails every 5 minutes.
     * Emails with status ERROR will be retried.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000) // runs every 5 minutes
    public void retryFailedEmails(){
        // Find all emails with ERROR status
        List<Email> failed = emailRepository.findByStatus(Status.ERROR);

        // If no failed emails, do nothing
        if (failed.isEmpty()){
            return;
        }

        // Retry sending each failed email
        for (Email email : failed){
            // Increment retry count
            email.setRetryCount(email.getRetryCount() + 1);
            // Update the last attempt timestamp
            email.setLastAttempt(Instant.now());

            // Attempt to send the email again
            emailService.sendEmail(email);
        }

    }

}
