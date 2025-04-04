package com.fasfood.notificationservice.infrastructure.support.util;

import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.notificationservice.infrastructure.persistence.entity.EmailTemplateEntity;
import com.fasfood.notificationservice.infrastructure.persistence.repository.EmailTemplateEntityRepository;
import com.fasfood.notificationservice.infrastructure.support.exception.NotFoundError;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;
    private final EmailTemplateEntityRepository emailTemplateEntityRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(SendEmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(this.fromEmail);
            helper.setSubject(request.getSubject());

            String[] recipients = request.getTo().toArray(new String[0]);
            helper.setTo(recipients);

            String htmlContent = this.buildHtmlContent(request);
            helper.setText(htmlContent, true);

            this.mailSender.send(message);
        } catch (MessagingException e) {
            throw new ResponseException(InternalServerError.UNABLE_TO_SEND_EMAIL);
        }
    }

    private String buildHtmlContent(SendEmailRequest request) {
        EmailTemplateEntity template = this.emailTemplateEntityRepository.findByCode(request.getTemplateCode())
                .orElseThrow(() -> new ResponseException(NotFoundError.TEMPLATE_NOTFOUND, request.getTemplateCode()));
        String response = template.getBody();
        for (Map.Entry<String, Object> entry : request.getVariables().entrySet()) {
            response = response.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return response;
    }
}
