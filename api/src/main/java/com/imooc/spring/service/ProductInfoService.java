package com.imooc.spring.service;

import com.imooc.spring.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/21
 * 商品信息服务
 */

public interface ProductInfoService {

    /**
     * 根据商品id查询商品
     * @param id  商品id
     * @return  该id对应的商品
     */
    ProductInfo findOne(String id);

    /**
     * 查询所有上架商品
     * @return  所有状态为上架的商品
     */
    List<ProductInfo> findAllUp();

    /**
     * 查询所有商品 分页
     * @param pageable  分页信息
     * @return  所有商品分页信息
     */
    Page<ProductInfo> pageAll(Pageable pageable);

    /**
     * 保存商品
     * @param productInfo  商品信息
     * @return  已保存的商品信息
     */
    ProductInfo save(ProductInfo productInfo);
}
