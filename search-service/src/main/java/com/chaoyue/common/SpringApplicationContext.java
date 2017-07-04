package com.chaoyue.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器上下文
 * Created by chaoyue on 2017/5/23.
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.context = applicationContext;
    }

    public static <T> T getBean(String beanName){return (T) context.getBean(beanName);}

    public static <T> T getBean(Class<T> clazz){return (T) context.getBean(clazz);}
}
