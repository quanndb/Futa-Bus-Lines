package com.fasfood.notificationservice.application.service.cmd.impl;

import com.fasfood.notificationservice.application.dto.mapper.EmailTemplateDTOMapper;
import com.fasfood.notificationservice.application.dto.request.CreateOrUpdateEmailTemplateRequest;
import com.fasfood.notificationservice.application.dto.response.EmailTemplateDTO;
import com.fasfood.notificationservice.application.mapper.NotificationCmdMapper;
import com.fasfood.notificationservice.application.service.cmd.EmailTemplateCmdService;
import com.fasfood.notificationservice.domain.EmailTemplate;
import com.fasfood.notificationservice.domain.cmd.CreateOrUpdateEmailTemplateCmd;
import com.fasfood.notificationservice.domain.repository.EmailTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class EmailTemplateCmdServiceImpl implements EmailTemplateCmdService {

    private final NotificationCmdMapper cmdMapper;
    private final EmailTemplateDTOMapper dtoMapper;
    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    @Transactional
    public EmailTemplateDTO create(CreateOrUpdateEmailTemplateRequest request) {
        CreateOrUpdateEmailTemplateCmd cmd = this.cmdMapper.cmdFromRequest(request);
        EmailTemplate newOne = new EmailTemplate(cmd);
        newOne = this.emailTemplateRepository.save(newOne);
        return this.dtoMapper.domainToDTO(newOne);
    }

    @Override
    @Transactional
    public EmailTemplateDTO update(UUID id, CreateOrUpdateEmailTemplateRequest request) {
        EmailTemplate template = this.emailTemplateRepository.getById(id);
        CreateOrUpdateEmailTemplateCmd cmd = this.cmdMapper.cmdFromRequest(request);
        template = this.emailTemplateRepository.save(template.update(cmd));
        return this.dtoMapper.domainToDTO(template);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        EmailTemplate template = this.emailTemplateRepository.getById(id);
        template.delete();
        this.emailTemplateRepository.save(template);
    }

    @Override
    @Transactional
    public List<EmailTemplateDTO> createTemplates(List<MultipartFile> fileList) throws IOException {
        List<EmailTemplate> templates = new ArrayList<>();
        for (MultipartFile file : fileList) {
            templates.add(new EmailTemplate(CreateOrUpdateEmailTemplateCmd.builder()
                    .body(new String(file.getBytes(), StandardCharsets.UTF_8))
                    .name(file.getOriginalFilename())
                    .build()));
        }
        templates = this.emailTemplateRepository.saveAll(templates);
        return this.dtoMapper.domainToDTO(templates);
    }
}
