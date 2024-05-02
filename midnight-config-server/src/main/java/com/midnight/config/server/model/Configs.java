package com.midnight.config.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configs {

    private String app;
    private String ns;
    private String env;
    private String pkey;
    private String pval;

    public static Configs build(String app, String ns, String env, String pkey, String pval) {
        return new Configs(app, ns, env, pkey, pval);
    }

}
