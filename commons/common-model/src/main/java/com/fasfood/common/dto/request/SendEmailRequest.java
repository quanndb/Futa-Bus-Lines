package com.fasfood.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class SendEmailRequest {
    @NotBlank(message = "TEMPLATE_CODE_REQUIRED")
    private String templateCode;
//    @NotBlank(message = "SENDER_REQUIRED")
//    private String from;
    @NotEmpty(message = "RECIPIENTS_REQUIRED")
    private List<String> to;
    @NotBlank(message = "SUBJECT_REQUIRED")
    private String subject;
    private Map<String, Object> variables;
}
