package com.imooc.spring.repository;

import com.imooc.spring.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangxishuo on 2018/6/3
 * 订单仓库层
 */

public interface OrderRepository extends JpaRepository<OrderMaster, Long> {

    Page<OrderMaster> findAllByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
