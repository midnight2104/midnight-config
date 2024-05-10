package com.midnight.config.client.config;

import com.midnight.config.client.value.SpringValueProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 手动注册bean
 */
@Slf4j
public class MidnightRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);
    }

    private void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        log.info("register {}", aClass);

        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(x -> Objects.equals(x, aClass.getName()))
                .findFirst();

        if (first.isPresent()) {
            log.info("{} class already registered", aClass);
            return;
        }


        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(aClass)
                .getBeanDefinition();

        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
    }
}
