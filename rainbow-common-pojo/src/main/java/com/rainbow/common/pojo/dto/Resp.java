package com.rainbow.common.pojo.dto;

import com.rainbow.common.pojo.enums.ReturnCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @Desc 通用返回
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description = "通用响应")
public class Resp<T> extends BaseDto {
    @Valid
    @ApiModelProperty(value = "通用响应：响应头，携带通用数据")
    private RespHead head = new RespHead();

    @Valid
    @ApiModelProperty(value = "通用响应：响应体")
    private T body;

    @ApiModelProperty(value = "签名", example="4EABF9EDBECFE4E9084181D6B5D59A92")
    private String sign;

    public Resp(T body) {
        this.head.setCode(ReturnCodeEnum.SUCCESS.getCode());
        this.head.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        this.body = body;
    }
}
