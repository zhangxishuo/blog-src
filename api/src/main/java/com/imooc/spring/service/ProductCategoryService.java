package com.imooc.spring.service;

import com.imooc.spring.entity.ProductCategory;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类别service
 */

public interface ProductCategoryService {

    /**
     * 根据id查询商品类目
     * @param categoryId     类目id
     * @return    商品类目
     */
    ProductCategory findOne(Integer categoryId);

    /**
     * 查询所有商品类目
     * @return    所有商品类目
     */
    List<ProductCategory> findAll();

    /**
     * 根据商品类目编号数组查询相关类目
     * @param typeList 类目编号数组
     * @return    相关类目
     */
    List<ProductCategory> findByTypeIn(List<Integer> typeList);

    /**
     * 保存商品类别
     * @param productCategory 商品类别
     * @return    保存后的商品类别
     */
    ProductCategory save(ProductCategory productCategory);
}
