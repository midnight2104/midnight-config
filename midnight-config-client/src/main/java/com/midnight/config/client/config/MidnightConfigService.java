package com.midnight.config.client.config;

import com.midnight.config.client.repository.MidnightRepository;
import com.midnight.config.client.repository.MidnightRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface MidnightConfigService extends MidnightRepositoryChangeListener {

    static MidnightConfigService getDefault(ApplicationContext context, ConfigMeta meta) {
        MidnightRepository repository = MidnightRepository.getDefault(context, meta);
        Map<String, String> config = repository.getConfig();
        MidnightConfigServiceImpl configService = new MidnightConfigServiceImpl(context, config);
        repository.addListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);

}
