package com.midnight.config.client.config;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义属性处理器
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {
    private final static String MID_PROPERTY_SOURCES = "MidPropertySources";
    private final static String MID_PROPERTY_SOURCE = "KKPropertySource";

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        // 避免重复加载
        if (env.getPropertySources().contains(MID_PROPERTY_SOURCES)) {
            return;
        }

        // todo 通过http请求到 config-server 获取配置
        Map<String, String> config = new HashMap<>();
        config.put("midnight.a", "dev500");
        config.put("midnight.b", "b600");
        config.put("midnight.c", "c700");

        MidnightConfigService configService = new MidnightConfigServiceImpl(config);
        MidnightPropertySource propertySource = new MidnightPropertySource(MID_PROPERTY_SOURCE, configService);
        // CompositePropertySource可以加载多个属性源
        CompositePropertySource composite = new CompositePropertySource(MID_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);

        // 放到属性源环境变量的最前面
        env.getPropertySources().addFirst(composite);

    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
