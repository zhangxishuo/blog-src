package com.imooc.spring.service;

import com.imooc.spring.view.ProductView;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/25
 * 买家商品服务
 */

public interface BuyerProductService {

    /**
     * 获取所有商品信息(包括类目)
     * @return   List<ProductView>  商品类目及商品信息列表
     */
    List<ProductView> getAllProductView();
}
