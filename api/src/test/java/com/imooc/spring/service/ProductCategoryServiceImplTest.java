package com.imooc.spring.service;

import com.imooc.spring.ApplicationTests;
import com.imooc.spring.entity.ProductCategory;
import com.imooc.spring.repository.ProductCategoryRepository;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类别service实现类测试
 */

public class ProductCategoryServiceImplTest extends ApplicationTests {

    private static final Logger logger = Logger.getLogger(ProductCategoryServiceImplTest.class);

    @Autowired
    private ProductCategoryRepository productCategoryRepository;     // 商品类目仓库

    @Autowired
    private ProductCategoryService productCategoryService;     // 商品类目服务

    @Test
    public void findOne() {

        logger.debug("新建类目并保存");
        ProductCategory productCategory = new ProductCategory();
        productCategoryRepository.save(productCategory);

        logger.debug("查询并断言");
        ProductCategory productCategory1 = productCategoryService.findOne(productCategory.getId());
        assertThat(productCategory1).isNotNull();
    }

    @Test
    public void findAll() {

        logger.debug("新建类目并保存");
        ProductCategory productCategory = new ProductCategory();
        productCategoryRepository.save(productCategory);

        logger.debug("查询并断言");
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        assertThat(productCategoryList).isNotNull();
    }

    @Test
    public void findByTypeIn() {

        logger.debug("新建并保存商品类型");
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setName("测试商品类型1");
        productCategory1.setType(1);
        productCategoryRepository.save(productCategory1);

        logger.debug("新建并保存商品类型");
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setName("测试商品类型2");
        productCategory2.setType(2);
        productCategoryRepository.save(productCategory2);

        logger.debug("查询");
        List<Integer> list = Arrays.asList(productCategory1.getType(), productCategory2.getType());
        List<ProductCategory> productCategoryList = productCategoryService.findByTypeIn(list);

        logger.info("断言");
        assertThat(productCategoryList.size()).isEqualTo(2);
    }

    @Test
    public void save() {

        logger.debug("新建类目并保存");
        ProductCategory productCategory = new ProductCategory();
        ProductCategory newProductCategory = productCategoryService.save(productCategory);

        logger.debug("断言");
        assertThat(newProductCategory).isNotNull();
    }
}