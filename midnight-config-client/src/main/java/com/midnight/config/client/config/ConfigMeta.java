package com.midnight.config.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMeta {

    private String app;
    private String env;
    private String ns;
    private String configServer;

    public String genKey() {
        return app + "_" + env + "_" + ns;
    }

    public String listPath() {
        return path("list");
    }

    public String versionPath() {
        return path("version");
    }

    private String path(String context) {
        return configServer + "/" + context + "?app=" + app + "&env=" + env + "&ns=" + ns;
    }
}
