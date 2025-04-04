package com.fasfood.notificationservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.notificationservice.application.dto.request.CreateOrUpdateEmailTemplateRequest;
import com.fasfood.notificationservice.domain.cmd.CreateOrUpdateEmailTemplateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationCmdMapper extends CmdMapper<CreateOrUpdateEmailTemplateCmd, CreateOrUpdateEmailTemplateRequest> {
}
