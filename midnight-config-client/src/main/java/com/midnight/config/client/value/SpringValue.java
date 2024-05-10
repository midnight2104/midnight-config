package com.midnight.config.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@AllArgsConstructor
@Data
public class SpringValue {
    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;

}
