package com.shiro.demo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidateDeserializer {

    public static class ObjectDeserializer<T> extends JsonDeserializer<T> {

        private final Class<T> objectType;

        public ObjectDeserializer(Class<T> objectType) {
            this.objectType = objectType;
        }


        @Override
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            Object value = null;
            //当前字段名
            String fieldName = jsonParser.getParsingContext().getCurrentName();
            //当前字段所属类
            String errorMsg = fieldName + "数值类型错误";
            Class<?> clazz = objectType;

            try {
                String text = jsonParser.getText();
                if (null==text || StringUtils.isEmpty(text.trim())) {
                    return null;
                }

                if (clazz.equals(Integer.class)) {
                    value = Integer.parseInt(text);
                } else if (clazz.equals(LocalDateTime.class)) {
                    value = LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } else if (clazz.equals(LocalDate.class)) {
                    value = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else if (clazz.equals(Long.class)) {
                    value = Long.parseLong(text);
                } else if (clazz.equals(Double.class)) {
                    value = Double.parseDouble(text);
                } else if (clazz.equals(BigDecimal.class)) {
                    value = new BigDecimal(text);
                }
            } catch (Exception e) {
                throw new DeserializerException(errorMsg);
            }
            return (T) value;
        }
    }

}
