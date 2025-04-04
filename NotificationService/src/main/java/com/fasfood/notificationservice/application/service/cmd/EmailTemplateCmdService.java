package com.fasfood.notificationservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.notificationservice.application.dto.request.CreateOrUpdateEmailTemplateRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EmailTemplateCmdService extends CmdService<EmailTemplateDTO, CreateOrUpdateEmailTemplateRequest, UUID> {
    List<EmailTemplateDTO> createTemplates(List<MultipartFile> fileList) throws IOException;
}