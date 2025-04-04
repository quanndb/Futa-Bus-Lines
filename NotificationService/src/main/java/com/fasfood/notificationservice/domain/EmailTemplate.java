package com.fasfood.notificationservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.notificationservice.domain.cmd.CreateOrUpdateEmailTemplateCmd;
import com.fasfood.util.RandomCodeUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class EmailTemplate extends Domain {
    private String name;
    private String description;
    private String code;
    private String body;

    public EmailTemplate(CreateOrUpdateEmailTemplateCmd cmd) {
        super();
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        this.code = RandomCodeUtil.generateOrderCode("TP-");
        this.body = cmd.getBody();
    }

    public EmailTemplate update(CreateOrUpdateEmailTemplateCmd cmd) {
        this.name = cmd.getName();
        this.description = cmd.getDescription();
        this.body = cmd.getBody();
        return this;
    }
}
