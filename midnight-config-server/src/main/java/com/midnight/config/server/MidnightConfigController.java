package com.midnight.config.server;

import com.alibaba.fastjson.JSONObject;
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
    ConfigsMapper configsMapper;


    Map<String, Long> lastTimestampMap = new HashMap<>();
    Map<String, List<Configs>> lastConfigMap = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(String app, String ns, String env) {
        String key = app + "_" + ns + "_" + env;
        if (lastConfigMap.containsKey(key)) return lastConfigMap.get(key);
        List<Configs> configs = configsMapper.selectAll(app, ns, env);
        lastConfigMap.put(key, configs);
        System.out.println(" =====>>>> current configs: " + configs);
        return configs;
    }

    @PostMapping("/update")
    public List<Configs> update(@RequestParam("app") String app, @RequestParam("ns") String ns,
                                @RequestParam("env") String env, @RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        insertOrUpdateAll(convertJSON2Configs(app, ns, env, jsonObject));
        List<Configs> configsList = configsMapper.selectAll(app, ns, env);

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

}
