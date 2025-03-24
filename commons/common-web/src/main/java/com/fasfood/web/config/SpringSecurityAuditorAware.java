package com.fasfood.web.config;

import com.fasfood.web.support.SecurityUtils;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

@NoArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUser().orElse("anonymous"));
    }
}
