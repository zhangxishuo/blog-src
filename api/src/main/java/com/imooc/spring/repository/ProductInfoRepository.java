package com.imooc.spring.repository;

import com.imooc.spring.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品信息仓库
 */

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    /**
     * 根据状态查询商品信息
     * @param status  商品状态
     * @return  符合该状态的所有商品
     */
    List<ProductInfo> findAllByStatus(Integer status);
}
