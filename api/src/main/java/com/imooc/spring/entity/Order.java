package com.imooc.spring.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author zhangxishuo on 2018/4/25
 * 订单主表
 */

@Entity
public class Order {

    public static final Integer NEW = 0;         // 新下单

    public static final Integer FINISH = 1;      // 已完结

    public static final Integer CANCEL = 2;      // 已取消

    public static final Integer WAIT =  0;       // 未支付

    public static final Integer SUCCESS = 1;     // 支付成功

    @Id
    private String id;                    // 订单id

    private String buyerName;             // 买家姓名

    private String buyerPhone;            // 买家电话

    private String buyerAddress;          // 买家地址

    private String buyerOpenid;           // 买家Openid

    private BigDecimal amount;            // 总金额

    private Integer status = NEW;         // 订单状态

    private Integer payStatus = WAIT;     // 支付状态

    @CreationTimestamp
    private Calendar createTime;          // 创建时间

    @UpdateTimestamp
    private Calendar updateTime;          // 更新时间

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerOpenid() {
        return buyerOpenid;
    }

    public void setBuyerOpenid(String buyerOpenid) {
        this.buyerOpenid = buyerOpenid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
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
