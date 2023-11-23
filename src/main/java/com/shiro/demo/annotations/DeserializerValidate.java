package com.shiro.demo.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shiro.demo.config.ValidateDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = ValidateDeserializer.ObjectDeserializer.class)
public @interface DeserializerValidate {
    Class<? extends JsonDeserializer<?>> jsonDeserializeClass() default JsonDeserializer.None.class;

    String errorMsg() default "";

}
