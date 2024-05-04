package com.midnight.config.client.config;

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
public class MidnightRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        System.out.println("register PropertySourcesProcessor");

        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(x -> Objects.equals(x, PropertySourcesProcessor.class.getName()))
                .findFirst();

        if (first.isPresent()) {
            System.out.println("PropertySourcesProcessor already registered");
            return;
        }


        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(PropertySourcesProcessor.class)
                .getBeanDefinition();

        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);
    }
}
