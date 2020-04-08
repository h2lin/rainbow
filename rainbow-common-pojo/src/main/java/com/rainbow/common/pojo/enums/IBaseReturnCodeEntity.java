package com.rainbow.common.pojo.enums;

/**
 * @Desc 返回状态码
 *
 * <ul>
 * <li>XXX1XX：信息类
 * <li>XXX2XX：操作成功
 * <li>XXX3XX：重定向
 * <li>XXX4XX：客户端错误
 * <li>XXX5XX：服务器错误
 * </ul>
 *
 * @Author wuzh
 * @Date 2020/4/8
 */
public interface IBaseReturnCodeEntity {
    String getCode();
    String getMsg();
}
