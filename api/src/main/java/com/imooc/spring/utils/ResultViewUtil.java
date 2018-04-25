package com.imooc.spring.utils;

import com.imooc.spring.view.Result;

/**
 * @author zhangxishuo on 2018/4/25
 * 返回结果视图工具包
 */

public class ResultViewUtil {

    public static Result success(Object object) {
        Result result = new Result();
        result.setData(object);
        result.setCode(Result.SUCCESS);
        result.setMsg("成功");
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
