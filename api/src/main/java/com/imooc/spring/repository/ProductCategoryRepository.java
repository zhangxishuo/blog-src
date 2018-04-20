package com.imooc.spring.repository;

import com.imooc.spring.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类目仓库
 */

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    /**
     * 根据商品编号数组获取所有相关商品类目
     * @param typeList  商品编号数组
     * @return  相关的商品类目
     */
    List<ProductCategory> findAllByTypeIn(List<Integer> typeList);
}
