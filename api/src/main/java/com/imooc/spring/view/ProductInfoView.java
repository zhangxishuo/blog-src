package com.imooc.spring.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhangxishuo on 2018/4/22
 * 商品信息对象
 */

@Data
@NoArgsConstructor
public class ProductInfoView {

    private String id;               // 商品id

    private String name;             // 商品名称

    private BigDecimal price;        // 商品单价

    private String description;      // 商品描述

    private String icon;             // 商品图片
}
