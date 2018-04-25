package com.imooc.spring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author zhangxishuo on 2018/4/25
 * 订单详情表
 */

@Entity
@Data
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;                   // 订单详情id

    @ManyToOne
    private Order order;                  // 订单

    @ManyToOne
    private ProductInfo productInfo;      // 商品

    private Integer productQuantity;      // 商品数量

    @CreationTimestamp
    private Calendar createTime;          // 创建时间

    @UpdateTimestamp
    private Calendar updateTime;          // 更新时间
}
