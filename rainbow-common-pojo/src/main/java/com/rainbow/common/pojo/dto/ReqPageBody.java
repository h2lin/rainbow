package com.rainbow.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Desc 通用请求：分页请求体
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description = "通用请求：分页请求体")
public class ReqPageBody<T> extends BaseDto {
//    private T query;
//
//    private int pageIndex;
//    private int pageSize;
//
//    @ApiModelProperty(value = "是否包含总数量", example = "true")
//    private boolean containTotalCount = true;
}
