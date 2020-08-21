package com.rainbow.starter.mybatis.autoconfig;

import com.rainbow.starter.common.constant.SymbolConstant;
import com.rainbow.starter.mybatis.constants.RbowDatasourceConstant;
import com.rainbow.starter.mybatis.plugin.MybatisSqlLogInterceptor;
import com.rainbow.starter.mybatis.properties.RbowDatasourceProperties;
import com.rainbow.starter.mybatis.properties.RbowSingleDatasourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Desc 数据源初始化，抽象类
 * @Author wuzh
 * @Date 2020/7/1
 */
public abstract class AbstractDatasourceInitInvoker {
     private final ConfigurableBeanFactory beanFactory;

     protected MybatisSqlLogInterceptor mybatisSqlLogInterceptor;


    // 在构造方法中初始化。加载Bean时即可自动执行。且自动获取入参的Bean。
    public AbstractDatasourceInitInvoker(final ConfigurableBeanFactory beanFactory,
             final RbowDatasourceProperties rbowDatasourceProperties) {
        // 注入Mybatis日志拦截器插件Bean
        this.mybatisSqlLogInterceptor = new MybatisSqlLogInterceptor();

        this.beanFactory = beanFactory;
        this.injectDataSources(rbowDatasourceProperties);
    }

    // 把全部的数据源都进行注入到容器
    private void injectDataSources(RbowDatasourceProperties rbowDatasourceProperties) {
        // 1.多数据源配置校验
        this.validDatasourceProperties(rbowDatasourceProperties);

        // 2.根据多数据源配置将多数据源Bean注册至容器
        this.registDatasources(rbowDatasourceProperties);
    }

    // 多数据源配置校验
    private void validDatasourceProperties(RbowDatasourceProperties rbowDatasourceProperties) {
        // 1.入参不可为空
        Map<String, RbowSingleDatasourceProperties> dsProps = rbowDatasourceProperties.getDatasources();
        if (CollectionUtils.isEmpty(dsProps)) {
            throw new IllegalStateException("rbow.datasources未配置！");
        }

        // 2.各数据源的各个属性不可为空
        for (Map.Entry<String, RbowSingleDatasourceProperties> dsProp : dsProps.entrySet()) {
            RbowSingleDatasourceProperties properties = dsProp.getValue();

            boolean isAnyBlank = StringUtils.isEmpty(properties.getDriverClassName())
                    || StringUtils.isEmpty(properties.getJdbcurl())
                    || StringUtils.isEmpty(properties.getUsername())
                    || StringUtils.isEmpty(properties.getPassword())
                    || StringUtils.isEmpty(properties.getMapperInterfaceLocation())
                    || StringUtils.isEmpty(properties.getMapperXmlLocation());
            Assert.state(!isAnyBlank,
                    RbowSingleDatasourceProperties.class.getCanonicalName()+"attriutes未配置");
        }
    }

    // 根据多数据源配置将多数据源Bean注册至容器
    private void registDatasources(RbowDatasourceProperties rbowDatasourceProperties) {
        Map<String, RbowSingleDatasourceProperties> dsProps = rbowDatasourceProperties.getDatasources();

        dsProps.forEach((dsName, dsProp) -> {
            // 生成数据源
            DataSource ds = this.createDatasource(dsProp);
            // 注册数据源
            this.registDatasource(dsName, ds);
            // 初始化数据源。如：注入SqlSessionFactory和MapperScannerConfigurer等
            this.initDataSource(dsName, ds, dsProp);
        });
    }

    private DataSource createDatasource(RbowSingleDatasourceProperties properties) {
        return createHikariDataSource(properties);
    }

    private DataSource createHikariDataSource(RbowSingleDatasourceProperties dsProp) {
        HikariDataSource hds = new HikariDataSource();
        hds.setDriverClassName(dsProp.getDriverClassName());
        hds.setJdbcUrl(dsProp.getJdbcurl());
        hds.setUsername(dsProp.getUsername());
        hds.setPassword(dsProp.getPassword());
        //Hikari连接池其他配置
        hds.setConnectionTimeout(dsProp.getConnectionTimeout());
        hds.setValidationTimeout(dsProp.getValidationTimeout());
        hds.setIdleTimeout(dsProp.getIdleTimeout());
        hds.setMaxLifetime(dsProp.getMaxLifetime());
        hds.setMaximumPoolSize(dsProp.getMaximumPoolSize());
        hds.setMinimumIdle(dsProp.getMinimumIdle());
        return hds;
    }

    /**
     * 注册数据源
     * @param dsName 该数据源在application.yml配置中的名字
     */
    private void registDatasource(String dsName, DataSource ds) {
        String dsBeanName = addSuffixBeanClassName(dsName, HikariDataSource.class.getSimpleName());
        this.registerBean(dsBeanName, ds);
    }

    // 注册Bean
    protected void registerBean(String beanName, Object singletonObject) {
        beanFactory.registerSingleton(beanName, singletonObject);
    }

    // 拼接Bean名称
    protected String addSuffixBeanClassName(String datasourceName, String beanClassName) {
        return datasourceName + beanClassName;
    }


    // 初始化数据源。如：注入SqlSessionFactory和MapperScannerConfigurer等
    private void initDataSource(String dsName, DataSource ds, RbowSingleDatasourceProperties dsProp) {
        // 配置SqlSessionFactoryBean
        String sqlSessionFactoryBeanName = registSqlSessionFactoryBean(dsName, ds, dsProp);

        // 配置MapperScannerConfiguer
        this.registMapperScannerConfigurer(dsName, sqlSessionFactoryBeanName, dsProp);

        // 配置DataSource的DataSourceTransactionManager
        String dataSourceTransactionManagerName = this.addSuffixBeanClassName(
                dsName, RbowDatasourceConstant.TRANSACTION_MANAGER_NAME_SUFFIX);
        this.registDataSourceTransactionManager(dataSourceTransactionManagerName, ds);

        // 保存数据源transactionBasePackagesName和数据源dataSourceTransactionManager的Map映射。
        // 方便后续方法上根据包路径找到对应的TransactionManager
        this.saveTransactionManagerInfo(dsProp.getTransactionBasePackages(), dataSourceTransactionManagerName);
    }

    // 注册SqlSessionFactoryBean，并返回SqlSessionFactory的Bean名称
    protected abstract String registSqlSessionFactoryBean(String dsName
            , DataSource ds, RbowSingleDatasourceProperties dsProp);

    // 注册MapperScannerConfigurer
    protected abstract void registMapperScannerConfigurer(String dsName
            , String sqlSessionFactoryBeanName, RbowSingleDatasourceProperties dsProp);

    // 注册DataSourceTransactionManager
    private void registDataSourceTransactionManager(String dataSourceTransactionManagerName, DataSource ds) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(ds);
        this.registerBean(dataSourceTransactionManagerName, dataSourceTransactionManager);
    }
    // 保存数据源basepackageName和数据源dataSourceTransactionManager的Map映射。方便后续方法上根据包路径找到对应的TransactionManager
    private void saveTransactionManagerInfo(String transactionBasePackagesName, String dataSourceTransactionManagerName) {
        if (org.apache.commons.lang3.StringUtils.isBlank(transactionBasePackagesName)) {
            return;
        }

        String[] transactionBasePackageArray = transactionBasePackagesName.split(SymbolConstant.COMMA);

        InitTransactionalAnnotationValue.getMultiTransactionManagerNameMap().putIfAbsent(
                transactionBasePackagesName, dataSourceTransactionManagerName);
    }
}
