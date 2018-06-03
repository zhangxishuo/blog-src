package com.imooc.spring.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品信息实体
 */

@Entity
public class ProductInfo {

    public static final Integer STATUS_NORMAL = 0;     // 正常
    public static final Integer STATUS_STOP   = 1;     // 下架

    @Id
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

    public ProductInfo() {
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }
}
