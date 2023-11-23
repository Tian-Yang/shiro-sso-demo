package com.shiro.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(LocalDateTime.class, new ValidateDeserializer.ObjectDeserializer(LocalDateTime.class));
        simpleModule.addDeserializer(LocalDate.class, new ValidateDeserializer.ObjectDeserializer(LocalDate.class));
        simpleModule.addDeserializer(Integer.class, new ValidateDeserializer.ObjectDeserializer(Integer.class));
        simpleModule.addDeserializer(Long.class, new ValidateDeserializer.ObjectDeserializer(Long.class));
        simpleModule.addDeserializer(BigDecimal.class, new ValidateDeserializer.ObjectDeserializer(BigDecimal.class));
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
