package com.midnight.config.client.config;

public interface MidnightConfigService {

    String[] getPropertyNames();

    String getProperty(String name);

}
