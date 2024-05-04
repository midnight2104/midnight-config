package com.midnight.config.client.annotation;

import com.midnight.config.client.config.MidnightRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({MidnightRegistrar.class})
public @interface EnableMidnightConfig {
}
