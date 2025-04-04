package com.fasfood.notificationservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.notificationservice.application.dto.request.CreateOrUpdateEmailTemplateRequest;
import com.fasfood.notificationservice.application.dto.request.EmailTemplatePagingRequest;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Emails resource")
@RequestMapping("/api/v1")
@Validated
public interface EmailController {
    @Operation(summary = "Send email")
    @PostMapping(value = "/emails")
    @PreAuthorize("hasPermission(null, 'email.send')")
    Response<Void> send(@RequestBody @Valid SendEmailRequest sendEmailRequest) throws JsonProcessingException;

    @Operation(summary = "Create email template")
    @PostMapping(value = "/templates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'email.create')")
    Response<List<EmailTemplateDTO>> createEmailTemplate(@RequestPart(name = "files") List<MultipartFile> files) throws IOException;

    @Operation(summary = "Update email template")
    @PostMapping(value = "/templates/{id}")
    @PreAuthorize("hasPermission(null, 'email.update')")
    Response<EmailTemplateDTO> updateEmailTemplate(@PathVariable UUID id,
                                                   @RequestBody @Valid CreateOrUpdateEmailTemplateRequest createEmailTemplateRequest);

    @Operation(summary = "Delete email template")
    @DeleteMapping(value = "/templates/{id}")
    @PreAuthorize("hasPermission(null, 'email.delete')")
    Response<Void> deleteEmailTemplate(@PathVariable UUID id);

    @Operation(summary = "Get email templates")
    @GetMapping(value = "/templates")
    @PreAuthorize("hasPermission(null, 'email.read')")
    PagingResponse<EmailTemplateDTO> getEmailTemplates(@ParameterObject EmailTemplatePagingRequest pagingRequest);

    @Operation(summary = "Get email template by id")
    @GetMapping(value = "/templates/{id}")
    @PreAuthorize("hasPermission(null, 'email.read')")
    Response<EmailTemplateDTO> getEmailTemplate(@PathVariable UUID id);
}
