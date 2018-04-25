package com.imooc.spring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
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
}
