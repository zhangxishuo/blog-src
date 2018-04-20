package com.imooc.spring.repository;

import com.imooc.spring.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类目仓库
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findAllByTypeIn(List<Integer> typeList);
}
