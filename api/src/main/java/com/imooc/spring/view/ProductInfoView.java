package com.imooc.spring.view;

import java.math.BigDecimal;

/**
 * @author zhangxishuo on 2018/4/22
 * 商品信息对象
 */

public class ProductInfoView {

    private String id;               // 商品id

    private String name;             // 商品名称

    private BigDecimal price;        // 商品单价

    private String description;      // 商品描述

    private String icon;             // 商品图片

    public ProductInfoView() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
