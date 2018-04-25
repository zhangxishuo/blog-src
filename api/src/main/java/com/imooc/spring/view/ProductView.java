package com.imooc.spring.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/22
 * 商品视图对象
 */

@Data
@NoArgsConstructor
public class ProductView {

    @JsonProperty("name")                 // 该属性序列化为json
    private String categoryName;          // 类目名

    @JsonProperty("type")
    private Integer categoryType;         // 类型

    @JsonProperty("foods")
    private List<ProductInfoView> productInfoViewList;      // 商品信息
}
