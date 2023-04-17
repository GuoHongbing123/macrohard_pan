package com.vpsair.common.utils;

/**
 * 统一返回
 * @author Shen && syf0412@vip.qq.com
 * @date 2021/10/26 17:00
 */
public class Result<T> {
    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;
    /**
     * 消息内容
     */
    private String msg = "success";
    /**
     * 时间戳
     */
    private Long timeStamp = System.currentTimeMillis();
    /**
     * 响应数据
     */
    private T data;

    public static Result<Object> success() {
        return new Result<>().ok();
    }

    public Result<T> ok() {
        return this;
    }

    public Result<T> ok(String msg, T data) {
        this.setMsg(msg);
        this.setData(data);
        return this;
    }

    public Result<T> ok(String msg) {
        return this.ok(msg, null);
    }

    public Result<T> ok(T data) {
        return this.ok("success", data);
    }

    public Result<T> ok(T data, String msg) {
        return this.ok(msg, data);
    }

    public Result<T> ok(Integer code, T data, String msg) {
        this.setMsg(msg);
        this.setCode(code);
        this.setData(data);
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg) {
        return this.error(500, msg);
    }

    public Result<T> error() {
        return this.error("未知异常，请联系管理员");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
