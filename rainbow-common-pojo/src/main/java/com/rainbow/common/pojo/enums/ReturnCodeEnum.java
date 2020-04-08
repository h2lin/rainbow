package com.rainbow.common.pojo.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Desc 返回状态码枚举
 * @Author zh wu
 * @Date 2020/4/8
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReturnCodeEnum implements IBaseReturnCodeEntity {
    /** 校验失败 */
    VALIDATE_FAIL("100101", "校验失败"),
    /** 数据不存在 */
    DATE_MISSING("100102", "数据不存在"),
    /** 数据已存在 */
    DATE_EXISTED("100103", "数据已存在"),
    /** 成功 */
    SUCCESS("100200", "成功"),
    /** 签名错误 */
    SIGN_ERROR("100400", "签名错误"),
    /** 无权限访问 */
    NO_ACCESS("100401", "无权限访问"),
    /** 请求url错误 */
    REQUEST_URL_ERROR("100404", "请求url错误"),
    /** 请求超时 */
    REQUEST_TIMEOUT("100408", "请求超时"),
    /** 重复提交 */
    REPEAT_SUBMIT("100409", "重复提交"),
    /** 参数不全 */
    PARAMETERS_MISSING("100412", "参数不全"),
    /** 请求方式不支持 */
    REQUEST_METHOD_NOT_SUPPORTED("100415", "请求方式不支持"),
    /** 请求类型不支持 */
    UNSUPPORTED_MEDIA_TYPE("100416", "请求类型不支持"),
    /** 获取锁失败 */
    GET_LOCK_FAIL("100417", "获取锁失败"),
    /** 上传文件大小超过限制 */
    UPLOAD_FILE_SIZE_EXCEEDED("100418", "上传文件大小超过限制"),
    /** 当前会话已失效，请重新登陆 */
    NOT_LOGGED_IN("100419", "当前会话已失效，请重新登录"),
    /** 服务器异常 */
    SERVER_ERROR("100500", "服务器异常"),
    /** 获取HttpServletRequest失败 */
    GET_HTTPSERVLETREQUEST_FAIL("100501", "获取HttpServletRequest失败"),
    /** 获取getHttpServletResponse失败 */
    GET_HTTPSERVLETRESPONSE_FAIL("100501", "获取getHttpServletResponse失败"),
    /** Token签名失败 */
    TOKEN_SIGN_ERROR("100502","Token签名失败"),
    /** Token已过期 */
    TOKEN_OVERTIME_ERROR("100503","Token已过期"),
    /** Token格式错误 */
    TOKEN_PATTERN_ERROR("100504","Token格式错误"),
    /** Token没有被正确构造 */
    TOKEN_STRUCTURE_ERROR("100505","Token没有被正确构造"),
    /** 非法参数异常 */
    TOKEN_PARAM_ERROR("100506","非法参数异常"),
    /** 密码错误 */
    LOGIN_PASSWORD_ERROR("100507","密码错误"),
    /** 用户不存在 */
    LONIN_USERNAME_ERROR("100508","用户不存在"),
    /** Token为空 */
    TOKEN_NULL_ERROR("100509","Token为空"),
    /** 取得用户的接口已过期 */
    INTERFACE_TIMEOUT_ERROR("100510"," 取得用户的接口已过期");

    private String code;
    private String msg;
}
