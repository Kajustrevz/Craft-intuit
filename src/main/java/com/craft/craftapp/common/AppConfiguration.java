package com.craft.craftapp.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Getter
@Slf4j
public class AppConfiguration {
    private final String environment;

    public AppConfiguration(@Value("${spring.profiles.active}") String environment) {
        log.info(
                "Setting variables as environment variables. Environment: {}",  environment);
        this.environment = environment;
    }
}
