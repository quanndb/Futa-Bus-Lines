package com.fasfood.notificationservice.infrastructure.support.util;

import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.web.support.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JsonMapper jsonMapper;
    private final EmailSender emailSender;

    @KafkaListener(topics = "emails", groupId = "email-group")
    public void listen(String message) throws JsonProcessingException {
        SendEmailRequest received = this.jsonMapper.fromJson(message, SendEmailRequest.class);
        this.emailSender.sendEmail(received);
        log.info("Sent email: {}", received);
    }
}