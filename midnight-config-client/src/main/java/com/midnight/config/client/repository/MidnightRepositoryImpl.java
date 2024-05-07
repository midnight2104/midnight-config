package com.midnight.config.client.repository;

import com.alibaba.fastjson.TypeReference;
import com.midnight.config.client.config.ConfigMeta;
import com.midnight.config.client.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MidnightRepositoryImpl implements MidnightRepository {
    private ConfigMeta meta;
    private Map<String, Long> versionMap = new HashMap<>();
    private Map<String, Map<String, String>> configMap = new HashMap<>();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private List<MidnightRepositoryChangeListener> listeners = new ArrayList<>();

    public MidnightRepositoryImpl(ApplicationContext context, ConfigMeta meta) {
        this.meta = meta;
        executor.scheduleWithFixedDelay(this::heartbeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public Map<String, String> getConfig() {
        // 一个ns下面所有的key
        String key = meta.genKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }


    @Override
    public void addListener(MidnightRepositoryChangeListener listener) {
        listeners.add(listener);
    }


    private Map<String, String> findAll() {
        String listPath = meta.listPath();

        log.info("===>>> list all configs from midnight config server.");

        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });

        Map<String, String> resMap = new HashMap<>();
        configs.forEach(c -> resMap.put(c.getPkey(), c.getPval()));
        return resMap;
    }


    private void heartbeat() {
        log.info("===> config client heart beat... ");
        String versionPath = meta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>() {
        });

        String key = meta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);

        // 发生了变化
        if (version > oldVersion) {
            log.info("===>>>  current=" + version + ", old=" + oldVersion);
            log.info("===>>>  need update new configs.");

            versionMap.put(key, version);

            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);

            // 发布事件
            listeners.forEach(l -> l.onChange(new MidnightRepositoryChangeListener.ChangeEvent(meta, newConfigs)));

        }
    }


}
