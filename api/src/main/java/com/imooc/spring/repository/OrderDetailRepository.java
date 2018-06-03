package com.imooc.spring.repository;

import com.imooc.spring.entity.Order;
import com.imooc.spring.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangxishuo on 2018/6/3
 * 订单详情表仓库层
 */

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findAllByOrder(Order order);
}
