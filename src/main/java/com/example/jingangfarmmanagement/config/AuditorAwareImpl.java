package com.example.jingangfarmmanagement.config;


import com.example.jingangfarmmanagement.uitl.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private ContextUtil contextUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            log.info("current: {}", contextUtil.getUserName());
            return Optional.of(contextUtil.getUserName());
        } catch (Exception e) {
            log.error("(getCurrentAuditor) error: {}", e.getMessage());
            return Optional.of("GUESS");
        }
    }
}
