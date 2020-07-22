package com.rainbow.starter.mybatis.autoconfig;

import com.github.pagehelper.PageInterceptor;
import com.rainbow.starter.common.support.beansupport.UniqueBeanNameGenerator;
import com.rainbow.starter.mybatis.constants.RbowDatasourceConstant;
import com.rainbow.starter.mybatis.properties.RbowDatasourceProperties;
import com.rainbow.starter.mybatis.properties.RbowSingleDatasourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @Desc 数据源配置初始化
 * @Author wuzh
 * @Date 2020/6/30
 */
@Slf4j
public class RbowDatasourceInitInvoker extends AbstractDatasourceInitInvoker {

    public RbowDatasourceInitInvoker(final ConfigurableBeanFactory beanFactory,
                                     final RbowDatasourceProperties rbowDatasourceProperties) {
        // 初始化逻辑在抽象类里
        super(beanFactory, rbowDatasourceProperties);
    }

    @Override
    protected String initSqlSessionFactory(String dsName, DataSource ds, RbowSingleDatasourceProperties dsProp) {
        // 1.生成Bean
        SqlSessionFactoryBean fcBean = new SqlSessionFactoryBean();
        fcBean.setDataSource(ds);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            fcBean.setMapperLocations(resolver.getResources(dsProp.getMapperXmlLocation()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        // 2.增加插件：PageHelper
        fcBean.setPlugins((new Interceptor[] { mybatisSqlLogInterceptor, this.buildPageInterceptor() }));

        // 2.注册Bean
        String sqlSessionFactoryBeanName = super.addSuffixBeanClassName(
                dsName, RbowDatasourceConstant.SQL_SESSION_FACTORY_BEAN_NAME_SUFFIX);
        super.registerBean(sqlSessionFactoryBeanName, fcBean);
        return sqlSessionFactoryBeanName;
    }

    // 注意：因为使用了tk，注入的是tk的MapperScannerConfigurer
    @Override
    protected void initMapperScannerConfigurer(
            String dsName, String sqlSessionFactoryBeanName, RbowSingleDatasourceProperties dsProp) {
        // 1.生成Bean
        MapperScannerConfigurer scanConf = new MapperScannerConfigurer();
        // 1.1 配置tk特有的属性
        Properties prop = new Properties();
        prop.setProperty("IDENTITY", "MYSQL");
        prop.setProperty("notEmpty", "true");
        prop.setProperty("safeUpdate", "true");
        scanConf.setProperties(prop);
        // 1.2 配置其他特性
        scanConf.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        scanConf.setBasePackage(dsProp.getMapperInterfaceLocation());
        scanConf.setNameGenerator(new UniqueBeanNameGenerator()); // 避免多数据源bean名称冲突

        // 2.注册Bean
        String scanConfBeanName = super.addSuffixBeanClassName(
                dsName, MapperScannerConfigurer.class.getSimpleName());
        // super.registerBean(scanConfBeanName, scanConf);
        // registerBean失效，必须采用postProcessBeanDefinitionRegistry注入
        scanConf.postProcessBeanDefinitionRegistry(RbowDatasourceAutoConfig.RegistPostProcessor.getRegistry());
    }

    /**
     * Mybatis插件：分页拦截器
     * @return
     */
    private PageInterceptor buildPageInterceptor() {
        PageInterceptor pageHelper = new PageInterceptor();
        Properties p = new Properties();
        // 分页合理化参数
        // 1.开启优化，如果开启优化，在分页页码结果没有数据的时候,会显示有数据的页码数据
        p.setProperty("reasonable", "true");
        // 2.是否支持接口参数来传递分页参数，默认false
        p.setProperty("supportMethodsArguments", "true");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
