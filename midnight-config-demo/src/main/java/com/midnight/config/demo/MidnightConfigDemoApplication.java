package com.midnight.config.demo;

import com.midnight.config.client.annotation.EnableMidnightConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({MidnightDemoConfig.class})
@EnableMidnightConfig
public class MidnightConfigDemoApplication {

    @Value("${midnight.a}")
    private String a;

    @Autowired
    private MidnightDemoConfig demoConfig;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(MidnightConfigDemoApplication.class, args);
    }


    @Bean
    public ApplicationRunner runner() {
        log.info("====> activeProfiles" + Arrays.toString(env.getActiveProfiles()));

        return args -> {
            log.info("====> midnight.a = " + a);
            log.info("====> demoConfig.a = " + demoConfig.getA());
        };
    }
}
