package com.imooc.spring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品信息实体
 */

@Entity
@Data
@NoArgsConstructor
public class ProductInfo {

    public static final Integer STATUS_NORMAL = 0;     // 正常
    public static final Integer STATUS_STOP   = 1;     // 下架

    private String id;           // 商品id

    private String name;         // 商品名称

    private BigDecimal price;    // 单价

    private Integer stock;       // 库存

    private String description;  // 描述

    private String icon;         // 图片

    private Integer status;      // 状态

    private Integer categoryType;  // 类目编号

    @CreationTimestamp
    private Calendar createTime;  // 创建时间

    @UpdateTimestamp
    private Calendar updateTime;  // 更新时间
}
