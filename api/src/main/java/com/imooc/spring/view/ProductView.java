package com.imooc.spring.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/22
 * 商品视图对象
 */

public class ProductView {

    @JsonProperty("name")                 // 该属性序列化为json
    private String categoryName;          // 类目名

    @JsonProperty("type")
    private Integer categoryType;         // 类型

    @JsonProperty("foods")
    private List<ProductInfoView> productInfoViewList;      // 商品信息

    public ProductView() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public List<ProductInfoView> getProductInfoViewList() {
        return productInfoViewList;
    }

    public void setProductInfoViewList(List<ProductInfoView> productInfoViewList) {
        this.productInfoViewList = productInfoViewList;
    }
}
