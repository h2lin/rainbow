package com.rainbow.starter.mybatis.constants;

import lombok.experimental.UtilityClass;

/**
 * @Desc 数据源配置时用到的常量
 * @Author wuzh
 * @Date 2020/7/1
 */
@UtilityClass
public class RbowDatasourceConstant {
    // 数据源默认驱动名
    public static final String DEFAULT_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    // SqlSessionFactoryBean的bean名称组成后缀
    public static final String SQL_SESSION_FACTORY_BEAN_NAME_SUFFIX = "SqlSessionFactoryBean";

//    /** 事务bean名称组成部分（后缀） */
//    public static final String TRANSACTION_MANAGER_NAME_SUFFIX = "DataSourceTransactionManager";

}
