package com.rainbow.common.pojo.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @Desc 所有实体对象的父类
 * @Author wuzh
 * @Date 2020/4/8
 */
@SuperBuilder       // 方便使用建造者模式快速构建对象。e：ChildClassName.builder().parentField("value").childFiled("value").build();
@AllArgsConstructor // 全参构造方法。@SuperBuilder 不会默认创建全参构造方法。
public class BaseDto implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    // 定制化toString
    @Override
    public String toString() {
        return JSON.toJSONString(this,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteBigDecimalAsPlain, SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.DisableCircularReferenceDetect/* 禁用“循环引用检测”*/);
    }
}
