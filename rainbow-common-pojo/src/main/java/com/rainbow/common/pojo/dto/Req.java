package com.rainbow.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @Desc 通用请求
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description = "通用请求")
public class Req<T> extends BaseDto {
    @Valid  // Spring自带
    @ApiModelProperty(value = "通用请求，请求头，携带通用数据")
    private ReqHead head;

    @Valid
    @ApiModelProperty(value = "通用请求，请求体")
    private T body;

    @ApiModelProperty(value = "签名", example = "4EABF9EDBECFE4E9084181D6B5D59A92")
    private String sign;
}
