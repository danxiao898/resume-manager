package com.breez.base.result;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 自定义响应结构
 */
@Data
public class BreezResult {

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public BreezResult() {
    }
    public BreezResult(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }
    public BreezResult(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public BreezResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BreezResult ok() {
        return new BreezResult(null);
    }
    public static BreezResult ok(String message) {
        return new BreezResult(message, null);
    }
    public static BreezResult ok(Object data) {
        return new BreezResult(data);
    }
    public static BreezResult ok(String message, Object data) {
        return new BreezResult(message, data);
    }

    public static BreezResult build(Integer code, String message) {
        return new BreezResult(code, message, null);
    }

    public static BreezResult build(Integer code, String message, Object data) {
        return new BreezResult(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 MengxueguResult 对象
     * @param json
     * @return
     */
    public static BreezResult format(String json) {
        try {
            return JSON.parseObject(json, BreezResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
