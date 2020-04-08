package com.rainbow.common.pojo.dto;

import com.rainbow.common.pojo.enums.IBaseReturnCodeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Desc 通用响应：响应头
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "通用响应：响应头")
public class RespHead extends BaseDto {
    @ApiModelProperty(value = "响应状态码", example = "100200")
    private String code;
    @ApiModelProperty(value = "响应提示信息", example = "success")
    private String msg;
//    @ApiModelProperty(value = "请求的唯一标识", example = "5b422d0b-26a0-4695-a326-8a33d173756a")
//    private String nonce;
//    @ApiModelProperty(value = "响应时间戳", example = "1554551377629")
//    private Long timestamp;

    // 使用“标准状态码 + 标准返回信息”赋值
    public RespHead(IBaseReturnCodeEntity returnCodeEntity) {
        if (returnCodeEntity == null) {
            return;
        }
        this.code = returnCodeEntity.getCode();
        this.msg = returnCodeEntity.getMsg();
    }

    // 使用“标准状态码 + 自定义返回信息”赋值
    public RespHead(IBaseReturnCodeEntity returnCodeEntity, String msg) {
        if (returnCodeEntity != null) {
            this.code = returnCodeEntity.getCode();
        }
        this.msg = msg;
    }

    // 使用“自定义状态码 + 自定义返回信息”赋值
    public RespHead(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
