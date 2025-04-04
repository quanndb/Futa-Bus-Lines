package com.fasfood.notificationservice.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateEmailTemplateRequest {
    private String name;
    private String description;
    private String body;
}
