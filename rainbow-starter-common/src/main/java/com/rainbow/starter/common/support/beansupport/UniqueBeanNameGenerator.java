package com.rainbow.starter.common.support.beansupport;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * @Desc 生成bean名称的规则
 * @Author wuzh
 * @Date 2020/7/6
 */
public class UniqueBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        // 1.为注解类的Bean就引用注解的value字段(不为null时)
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                // Explicit bean name found.
                return beanName;
            }
        }

        // 2.容器中没有该bean名称，则使用默认的bean名称（不含package，首字母小写）
        String defaultClassName = super.buildDefaultBeanName(definition);

        BeanFactory beanFactory = (BeanFactory) registry;
        if (!beanFactory.containsBean(defaultClassName)) {
            return defaultClassName;
        }

        /**
         * 3.容器中已有该bean名称的情况：如果容器中已经有Bean叫这个名字了，那么新加入的Bean将名称加上包的前缀。
         *   即：如果该bean名称不存在容器中，则按照className的规则生成；否则按照“package+className”的规则存在。
         *   这样就避免了“同一模块内，不同包，类名相同”导致的注入失败问题。例如，同名的Service，同名的Mapper。
         */
        return definition.getBeanClassName();
    }

}