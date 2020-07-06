package com.rainbow.starter.mybatis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @Desc 多数据源的配置元数据
 * @Author wuzh
 * @Date 2020/7/1
 */
@Getter
@Setter
@ConfigurationProperties(prefix="rbow")
public class RbowDatasourceProperties {
    // 多数据源配置
    private Map<String, RbowSingleDatasourceProperties> datasources;
}
