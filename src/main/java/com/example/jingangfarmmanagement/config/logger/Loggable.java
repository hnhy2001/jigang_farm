package com.example.jingangfarmmanagement.config.logger;

import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    ELogType value();
    String successMessage() default "";
    String errorMessage() default "";

}
