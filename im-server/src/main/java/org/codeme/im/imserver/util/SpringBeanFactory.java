package org.codeme.im.imserver.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBeanFactory
 *
 * @author walker lee
 * @date 2020/5/17
 */
@Component
public final class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext sContext;

    public static <T> T getBean(Class<T> c){
        return sContext.getBean(c);
    }


    public static <T> T getBean(String name,Class<T> clazz){
        return sContext.getBean(name,clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        sContext = applicationContext;
    }
}
