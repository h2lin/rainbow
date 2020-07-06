package com.rainbow.starter.mybatis.autoconfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;

import javax.annotation.Resource;

/**
 * @Desc RbowDatasourceInitInvoker初始化
 *   由@EnableTransactionManagement触发ProxyTransactionManagementConfiguration
 *   从而触发RbowDatasourceInitInvoker的初始化
 * @Author wuzh
 * @Date 2020/7/3
 */
public class RbowDatasourceInitPostProcessor implements BeanPostProcessor, Ordered {
    @Resource
    private BeanFactory beanFactory;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // ProxyTransactionManagementConfiguration的Bean由@Configuration类的@EnableTransactionManagement触发
        // 该Bean注入要早于dispatcherServlet
        if (bean instanceof ProxyTransactionManagementConfiguration) {
            // force initialization of this bean as soon as we see a
            // ProxyTransactionManagementConfiguration
            beanFactory.getBean(RbowDatasourceInitInvoker.class);
        }
        return bean;
    }
}
