package com.imooc.spring.view;

/**
 * @author zhangxishuo on 2018/4/22
 * http请求返回的最外层对象
 */

public class Result<T> {

    public static final Integer SUCCESS = 0;  // 成功

    private Integer code;     // 错误码

    private String msg;       // 提示信息

    private T data;           // 内容信息

    public Result() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
