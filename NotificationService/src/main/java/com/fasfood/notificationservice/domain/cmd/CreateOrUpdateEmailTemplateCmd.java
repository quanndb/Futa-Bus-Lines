package com.fasfood.notificationservice.domain.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrUpdateEmailTemplateCmd {
    private String name;
    private String description;
    private String body;
}
