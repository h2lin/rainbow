package com.rainbow.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Desc 通用请求：请求头
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description="通用请求：请求头")
public class ReqHead extends BaseDto {
    @ApiModelProperty(value = "用户Id", example = "2")
    private Long userId;
//    @ApiModelProperty(value = "用户名", example = "小明")
//    private String userName;
//    @ApiModelProperty(value = "登录令牌", example = "YWY5NDIyOGU0NDM1NDc5YmJkODE0ZmE1MDhjZDhkOWJfNTU0NDM2Mzk3NzMwMzUzMTUy")
//    private String token;
//    @ApiModelProperty(value = "请求唯一标识，uuid", example = "5b422d0b-26a0-4695-a326-8a33d173756a")
//    private String nonce;
//    @ApiModelProperty(value = "时间戳，单位毫秒", example = "1586336120857")
//    private String timestamp;
//
//
//    public static ReqHead getReqHead() {
//        // 缓存后续处理ReqHead reqHead = (ReqHead)ThreadLocalUtil.getCacheObject(ReqHead.class);
//        /*if (reqHead == null) {
//            reqHead = new ReqHead();
//        }*/
//
//        return new ReqHead();
//    }
}
