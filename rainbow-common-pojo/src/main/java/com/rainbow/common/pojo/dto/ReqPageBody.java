package com.rainbow.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Desc 通用请求：分页请求体
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description = "通用请求：分页请求体")
public class ReqPageBody<T> extends BaseDto {
    @ApiModelProperty(value = "分页请求参数（不包括分页信息）")
    @Valid
    private T query;

    @ApiModelProperty(value = "查询第几页，第1页值为1", required = true, example = "1")
    @NotNull
    @Min(value = 1)
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页显示几条数据", required = true, example = "10")
    @NotNull
    @Min(value = 1)
    private int pageSize = 20;

    @ApiModelProperty(value = "是否查询总数量", example = "true")
    private boolean containsTotalCount = true;
}
