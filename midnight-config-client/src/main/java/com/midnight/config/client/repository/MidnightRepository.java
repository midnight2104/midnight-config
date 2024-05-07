package com.midnight.config.client.repository;

import com.midnight.config.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface MidnightRepository {

    static MidnightRepository getDefault(ApplicationContext context, ConfigMeta meta) {
        return new MidnightRepositoryImpl(context, meta);
    }

    Map<String, String> getConfig();

    void addListener(MidnightRepositoryChangeListener listener);
}
