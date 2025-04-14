package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
public class FormTimeToTimeRequest extends Request {
    @Schema(type = "string", format = "time", example = "12:40:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime fromTime;
    @Schema(type = "string", format = "time", example = "12:40:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime toTime;
}
