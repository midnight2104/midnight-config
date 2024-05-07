package com.midnight.config.client.repository;

import com.midnight.config.client.config.ConfigMeta;

import java.util.Map;

public interface MidnightRepositoryChangeListener {

    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta meta, Map<String, String> config){}
}
