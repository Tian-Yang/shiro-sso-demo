package com.shiro.demo.context;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Spring HandlerMapping上下文
 * Author TianYang
 * Date 2023/11/22 09:53
 */
@Component
public class HandlerMappingContext implements ApplicationRunner {

    Map<String, HandlerMapping> handlerMappings;


    @Resource
    private ApplicationContext applicationContext;


    public Map<String, HandlerMapping> getHandlerMappings() {
        return this.handlerMappings;
    }

    @Override
    public void run(ApplicationArguments args) {
        handlerMappings = applicationContext.getBeansOfType(HandlerMapping.class);

    }
}
