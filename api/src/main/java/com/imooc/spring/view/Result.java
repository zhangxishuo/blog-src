package com.imooc.spring.view;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangxishuo on 2018/4/22
 * http请求返回的最外层对象
 */

@Data
@NoArgsConstructor
public class Result<T> {

    private Integer code;     // 错误码

    private String msg;       // 提示信息

    private T data;           // 内容信息
}
