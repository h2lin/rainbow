package com.rainbow.starter.mybatis.autoconfig;

import com.rainbow.starter.mybatis.properties.RbowDatasourceProperties;
import lombok.Getter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * @Desc 数据源相关的自动配置入口
 *
 * 功能：完成Bean的初始化工作：RbowDatasourceInitInvoker, RbowDatasourceInitPostProcessor,
 *      Datasource, SqlSessionFactoryBean, MapperScannerConfigurer(tk的)
 * 初始化顺序：
 * 1.ImportBeanDefinitionRegistrar 接口为入口，在该接口的方法中手动注入RbowDatasourceInitPostProcessor的Bean
 *   。对应下面的@Import({RbowDatasourceAutoConfig.RegistPostProcessor.class})
 * 2.RbowDatasourceInitPostProcessor的Bean实现BeanPostProcessor接口，该接口的
 *   postProcessAfterInitialization方法监听容器Bean注入情况，监听ProxyTransactionManagementConfiguration注入
 * 3.本类的@EnableTransactionManagement注解触发ProxyTransactionManagementConfiguration注入
 * 4.监听到ProxyTransactionManagementConfiguration的Bean注入后，在监听的方法中实现RbowDatasourceInitInvoker这
 *   个Bean的初始化。对应下面的@Import({RbowDatasourceInitInvoker.RegistPostProcessor.class})
 * 5.RbowDatasourceInitInvoker的初始化触发数据源的注入及初始化
 * 6.数据源直接根据配置生成注入
 * 7.SqlSessionFactoryBean根据配置生成后，使用beanFactory手动注入
 * 8.MapperScannerConfigurer的Bean使用beanFactory无效，需要手动使用BeanDefinitionRegistry方式注入
 * 9.多数据源注入，beanName一样时，需要手动生成Bean名称。手动增加了UniqueBeanNameGenerator。
 *
 * 总体流程：
 * -> @Import({RbowDatasourceAutoConfig.RegistPostProcessor.class})完成注入，入口
 * -> RbowDatasourceInitPostProcessor.class完成注入，并监听ProxyTransactionManagementConfiguration
 * -> @Import({RbowDatasourceInitInvoker.class})
 * -> @EnableTransactionManagement，触发ProxyTransactionManagementConfiguration，从而触发RbowDatasourceInitInvoker.class初始化
 * -> 数据源注入
 * -> 配套的SqlSessionFactoryBean注入
 * -> 配套的MapperScannerConfigurer注入
 * -> 有多个数据源则循环多次
 *
 * @Author wuzh
 * @Date 2020/6/30
 */
@Configuration
@EnableConfigurationProperties({RbowDatasourceProperties.class})
@Import({RbowDatasourceInitInvoker.class, RbowDatasourceAutoConfig.RegistPostProcessor.class})
@EnableTransactionManagement
public class RbowDatasourceAutoConfig {
//    @Bean
//    public InitTransactionalValue initTransactionalValue() {
//        return new InitTransactionalValue();
//    }

    // 注入RbowDatasourceInitPostProcessor
    public static class RegistPostProcessor implements ImportBeanDefinitionRegistrar {
        private static final String REGIST_BEAN_NAME = RbowDatasourceInitPostProcessor.class.getSimpleName();

        // 用于后续MapperScannerConfigurer的Bean注入
        @Getter
        private static BeanDefinitionRegistry registry;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                            BeanDefinitionRegistry registry) {
            if (registry.containsBeanDefinition(REGIST_BEAN_NAME)) {
                return;
            }

            RegistPostProcessor.registry = registry;    // 用于后续MapperScannerConfigurer的Bean注入

            // 注入RbowDatasourceInitPostProcessor
            GenericBeanDefinition beanDef = new GenericBeanDefinition();
            beanDef.setBeanClass(RbowDatasourceInitPostProcessor.class);
//            beanDef.setRole();??
            beanDef.setSynthetic(true); // 标记为由程序注入
            registry.registerBeanDefinition(REGIST_BEAN_NAME, beanDef);
        }
    }

    @Bean
    public InitTransactionalAnnotationValue initTransactionalAnnotationValue(final BeanFactory beanFactory) {
        return new InitTransactionalAnnotationValue(beanFactory);
    }
}
