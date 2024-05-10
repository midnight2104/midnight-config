package com.midnight.config.client.value;

import com.midnight.config.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 1. 扫描所有的Spring value，保存起来
 * 2. 在配置变更时，更新所有的spring value
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    private static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    private static final MultiValueMap<String, SpringValue> VALUE_Holder = new LinkedMultiValueMap<>();
    @Setter
    private BeanFactory beanFactory;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("[MidnightCONFIG] >> find spring value: {}", field);
            Value value = field.getAnnotation(Value.class);
            helper.extractPlaceholderKeys(value.value()).forEach(key -> {
                log.info("[MidnightCONFIG] >> find spring value: {}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                VALUE_Holder.add(key, springValue);
            });
        });

        return bean;
    }


    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("[MidnightCONFIG] >> update spring value for keys: {}", event.getKeys());
        event.getKeys().forEach(key -> {
            log.info("[MidnightCONFIG] >> update spring value: {}", key);
            List<SpringValue> springValues = VALUE_Holder.get(key);

            springValues.forEach(
                    springValue -> {
                        log.info("[MidnightCONFIG] >> update spring value: {} for key {}", springValue, key);

                        try {
                            Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
                                    springValue.getBeanName(), springValue.getPlaceholder());
                            log.info("[MidnightCONFIG] >> update value: {} for holder {}", value, springValue.getPlaceholder());

                            springValue.getField().setAccessible(true);
                            springValue.getField().set(springValue.getBean(), value);
                        } catch (Exception ex) {
                            log.error("[MidnightCONFIG] >> update spring value error", ex);
                        }

                    }
            );
        });
    }

}
