package com.midnight.config.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "midnight")
public class MidnightDemoConfig {
    private String a;
}
