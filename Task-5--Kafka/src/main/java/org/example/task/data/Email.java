package org.example.task.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "email-messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String to;

    @Field(type = FieldType.Keyword)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String sourceService;

    @Field(type = FieldType.Keyword)
    private Status status;

    @Field(type = FieldType.Date)
    private Instant createdAt = Instant.now();

    @Field(type = FieldType.Integer)
    private Integer retryCount = 0;

    @Field(type = FieldType.Date)
    private Instant lastAttempt;

    @Field(type = FieldType.Text)
    private String errorMessage;

    public Email(String to, String subject, String content, String sourceService, Status status, Instant createdAt) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.sourceService = sourceService;
        this.status = status;
        this.createdAt = createdAt;
    }
}
