package com.rainbow.starter.mybatis.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @Desc 由于配置的是多数据源，需要在初始化时，指定每个@Transactional的TransactionManager
 * @Author wuzh
 * @Date 2020/7/24
 */
@Slf4j
class InitTransactionalAnnotationValue implements ApplicationListener<ApplicationStartedEvent> {
    private BeanFactory beanFactory;

    private static final String ABSTRACT_FALLBACK_TRANSACTION_ATTRIBUTE_SOURCE_ATTRIBUTECACHE = "attributeCache";
    private static final String METHOD_CLASS_KEY_METHOD = "method";

    // 每个basePackage所对应的TransactionManagerName
    private static ConcurrentMap<String, String> multiTransactionManagerNameMap
            = new ConcurrentReferenceHashMap<>(4);

    // 提供给外部以初始化该map信息
    static ConcurrentMap<String, String> getMultiTransactionManagerNameMap() {
        return multiTransactionManagerNameMap;
    }

    InitTransactionalAnnotationValue(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        // 获取所有的事务信息
        AbstractFallbackTransactionAttributeSource transactionAttributeSource = beanFactory
                .getBean(AbstractFallbackTransactionAttributeSource.class);

        // 获取所有含有事务的方法的事务属性。
        // Key:具体的方法Key对象MethodClassKey，该对象包含了Method和它所在的类。Value：该方法上的@Transaction注解属性。
        Map<Object, TransactionAttribute> attributeCache = this.getField(transactionAttributeSource,
                AbstractFallbackTransactionAttributeSource.class,
                ABSTRACT_FALLBACK_TRANSACTION_ATTRIBUTE_SOURCE_ATTRIBUTECACHE);
        if (attributeCache == null || attributeCache.size() == 0) {
            return;
        }

        // 一个Entry就是一个@Transactional注解及其方法信息。
        // 根据方法的package路径找到该方法@Transactional注解应该配置的TransactionManager
        for (Map.Entry<Object, TransactionAttribute> entry : attributeCache.entrySet()) {
            // 已指明数据源或不是DefaultTransactionAttribute
            if (!StringUtils.isEmpty(entry.getValue().getQualifier())
                    || !(entry.getValue() instanceof DefaultTransactionAttribute)) {
                continue;
            }

            // 将TransactionManager配置到该方法@Transactional注解
            Method method = this.getField(entry.getKey(), MethodClassKey.class, METHOD_CLASS_KEY_METHOD);
            if (method == null) {
                continue;
            }

            boolean isTransactionalMethod = (AnnotationUtils.findAnnotation(method, Transactional.class) != null);
            if (!isTransactionalMethod) {
                continue;
            }

            DefaultTransactionAttribute transactionAttribute = (DefaultTransactionAttribute)entry.getValue();
            String packageName = method.getDeclaringClass().getPackage().getName();
            String transactionManagerName = this.getTransactionManagerName(packageName);
            transactionAttribute.setQualifier(transactionManagerName);
        }
    }

    // 根据包名，获取TransactionManager名称。该名称已于数据源初始化时存入内存
    private String getTransactionManagerName(String packageName) {
        for (Map.Entry<String, String> entry : multiTransactionManagerNameMap.entrySet()) {
            if (packageName.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
    }

    private <T> T getField(Object object, Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e){
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
