package com.rainbow.starter.mybatis.properties;


import com.rainbow.starter.mybatis.constants.RbowDatasourceConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * @Desc 单个数据源的配置元数据
 * @Author wuzh
 * @Date 2020/7/1
 */
@Getter
@Setter
public class RbowSingleDatasourceProperties {
    // jdbc驱动类名称，默认com.mysql.cj.jdbc.Driver
    private String driverClassName = RbowDatasourceConstant.DEFAULT_DRIVER_NAME;
    // jdbc的url
    private String jdbcurl;
    // 数据库用户名
    private String username;
    // 数据库密码
    private String password;
    // mapper接口扫描包路径。如com.xx.xxx
    private String mapperInterfaceLocation;
    // mapper.xml扫描文件路径。如classpath*:com/xxx/mapper/**/*Mapper.xml
    private String mapperXmlLocation;
    // 事务控制所在的顶层包名，多个包用英文逗号隔开。如com.xx.xxx
    private String transactionBasePackages;

    // =========================== Hikari连接池参数 ========================
    // 获取连接超时时间，默认30s
    private long connectionTimeout = 30000;
    // 验证一次数据库连接池连接是否为null的时间 默认3s
    private long validationTimeout = 3000;
    // 连接池最大连接（包括空闲和正在使用的连接）默认最大200
    private int maximumPoolSize = 200;
    // 连接池最小连接（包括空闲和正在使用的连接）默认最小10
    private int minimumIdle = 10;
    // 连接空闲时间。该设置仅适用于minimumIdle设置为小于maximumPoolSize的情况 默认:60000(1分钟)
    private long idleTimeout = 60000;
    // 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:10分钟
    private long maxLifetime = 600000;
}
