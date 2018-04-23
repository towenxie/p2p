/*
 * Copyright (c) 2016 Augmentum, Inc. All rights reserved.
 */
package edu.xmh.p2p.data.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * BeanFactory for spring.
 * 
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> classType) {
        return applicationContext.getBean(classType);
    }

    public static <T> T getBean(String beanName, Class<T> classType) {
        return applicationContext.getBean(beanName, classType);
    }
}
