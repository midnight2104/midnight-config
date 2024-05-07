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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({MidnightDemoConfig.class})
@EnableMidnightConfig
@RestController
public class MidnightConfigDemoApplication {
    @Value("${midnight.a}")
    private String a;

    @Value("${midnight.b}")
    private String b;

    @Autowired
    private MidnightDemoConfig demoConfig;

    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(MidnightConfigDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return "midnight.a = " + a + "\n"
                + "midnight.b = " + b + "\n"
                + "demoConfig.a = " + demoConfig.getA() + "\n"
                + "demoConfig.b = " + demoConfig.getB() + "\n";
    }

    @Bean
    ApplicationRunner applicationRunner() {
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        return args -> {
            System.out.println(a);
            System.out.println(demoConfig.getA());
        };
    }
}
