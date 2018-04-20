package com.imooc.spring.service;

import com.imooc.spring.entity.ProductCategory;
import com.imooc.spring.repository.ProductCategoryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类别service实现类
 */

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private static final Logger logger = Logger.getLogger(ProductCategoryServiceImpl.class);

    @Autowired
    private ProductCategoryRepository productCategoryRepository;     // 商品类目仓库

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return productCategoryRepository.findOne(categoryId);
    }

    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public List<ProductCategory> findByTypeIn(List<Integer> typeList) {
        return productCategoryRepository.findAllByTypeIn(typeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}
