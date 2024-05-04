package com.midnight.config.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * 自定义属性源
 */
public class MidnightPropertySource extends EnumerablePropertySource<MidnightConfigService> {

    public MidnightPropertySource(String name, MidnightConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
