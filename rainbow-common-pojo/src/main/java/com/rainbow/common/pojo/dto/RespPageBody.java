package com.rainbow.common.pojo.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Desc 通用请求：分页响应体
 * @Author wuzh
 * @Date 2020/4/8
 */
@Getter
@Setter
@ApiModel(description = "通用请求：分页响应体")
public class RespPageBody<T> extends BaseDto {
    @ApiModelProperty(value = "返回的数据")
    private List<T> datas;

    @ApiModelProperty(value = "页数。即当前是返回第几页的数据。")
    private int pageIndex;

    @ApiModelProperty(value = "页码大小。即当前设置为每页显示几条。")
    private int pageSize;

    @ApiModelProperty("总页数")
    private int pageCount;

    @ApiModelProperty(value = "数据总条数")
    private long totalCount;

//    @ApiModelProperty("其他扩展的数据")
//    private JSONObject otherDatas;

    public List<T> getDatas() {
        if (Objects.isNull(datas)) {
            datas = new ArrayList<>();
        }
        return datas;
    }
}
