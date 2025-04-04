package com.fasfood.notificationservice.presentation.rest.impl;

import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.notificationservice.application.dto.request.CreateOrUpdateEmailTemplateRequest;
import com.fasfood.notificationservice.application.dto.request.EmailTemplatePagingRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import com.fasfood.notificationservice.application.service.cmd.EmailTemplateCmdService;
import com.fasfood.notificationservice.application.service.query.EmailTemplateQueryService;
import com.fasfood.notificationservice.infrastructure.support.util.KafkaProducer;
import com.fasfood.notificationservice.presentation.rest.EmailController;
import com.fasfood.web.support.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmailControllerImpl implements EmailController {
    private final KafkaProducer kafkaProducer;
    private final JsonMapper jsonMapper;
    private final EmailTemplateCmdService cmdService;
    private final EmailTemplateQueryService queryService;

    @Override
    public Response<Void> send(SendEmailRequest sendEmailRequest) throws JsonProcessingException {
        this.kafkaProducer.sendMessage("emails", this.jsonMapper.toJson(sendEmailRequest));
        return Response.ok();
    }

    @Override
    public Response<List<EmailTemplateDTO>> createEmailTemplate(List<MultipartFile> files) throws IOException {
        return Response.of(this.cmdService.createTemplates(files));
    }

    @Override
    public Response<EmailTemplateDTO> updateEmailTemplate(UUID id, CreateOrUpdateEmailTemplateRequest request) {
        return Response.of(this.cmdService.update(id, request));
    }

    @Override
    public Response<Void> deleteEmailTemplate(UUID id) {
        this.cmdService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<EmailTemplateDTO> getEmailTemplates(EmailTemplatePagingRequest pagingRequest) {
        return PagingResponse.of(this.queryService.getList(pagingRequest));
    }

    @Override
    public Response<EmailTemplateDTO> getEmailTemplate(UUID id) {
        return Response.of(this.queryService.getById(id));
    }
}
