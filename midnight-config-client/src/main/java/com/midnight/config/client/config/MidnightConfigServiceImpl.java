package com.midnight.config.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;


@Slf4j
public class MidnightConfigServiceImpl implements MidnightConfigService {

    private Map<String, String> config;
    ApplicationContext applicationContext;

    public MidnightConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }

    @Override
    public void onChange(ChangeEvent event) {
        this.config = event.config();
        if (!config.isEmpty()) {
            // 发布环境变更事件
            // EnvironmentChangeEvent来自于SpringCloud
            log.info("===>>>  fire an EnvironmentChangeEvent with keys:" + config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
