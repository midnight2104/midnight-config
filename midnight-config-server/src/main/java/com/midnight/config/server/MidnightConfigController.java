package com.midnight.config.server;

import com.alibaba.fastjson.JSONObject;
import com.midnight.config.server.lock.DistributedLocks;
import com.midnight.config.server.mapper.ConfigsMapper;
import com.midnight.config.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class MidnightConfigController {
    @Autowired
    DistributedLocks locks;

    @Autowired
    ConfigsMapper configsMapper;
    Map<String, Long> VERSIONS = new HashMap<>();


    Map<String, Long> lastTimestampMap = new HashMap<>();
    Map<String, List<Configs>> lastConfigMap = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(String app, String ns, String env) {
        return configsMapper.selectAll(app, ns, env);
    }

    @PostMapping("/update")
    public List<Configs> update(@RequestParam("app") String app, @RequestParam("ns") String ns,
                                @RequestParam("env") String env, @RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        insertOrUpdateAll(convertJSON2Configs(app, ns, env, jsonObject));
        List<Configs> configsList = configsMapper.selectAll(app, ns, env);

        VERSIONS.put(app + "-" + env + "-" + ns, System.currentTimeMillis());

        return configsList;
    }

    private List<Configs> convertJSON2Configs(String app, String ns, String env, JSONObject jsonObject) {
        return jsonObject.entrySet().stream().map(
                        e -> Configs.build(app, ns, env, e.getKey(), e.getValue().toString()))
                .collect(Collectors.toList());
    }

    private void insertOrUpdateAll(List<Configs> result) {
        System.out.println("insert to db ..."); // consider as cache
        for (Configs c : result) {
            Configs configs = configsMapper.select(c.getApp(), c.getNs(), c.getEnv(), c.getPkey());
            if (configs == null) {
                System.out.println("configsMapper.insert( " + c + " )");
                configsMapper.insert(c);
            } else {
                System.out.println("configsMapper.update( " + c + " )");
                configsMapper.update(c);
            }
        }
    }

    @GetMapping("/version")
    public long version(String app, String env, String ns) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + ns, -1L);
    }

    @GetMapping("/status")
    public boolean status() {
        return locks.getLocked().get();
    }

}
