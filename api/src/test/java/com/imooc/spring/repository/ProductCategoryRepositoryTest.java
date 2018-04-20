package com.imooc.spring.repository;

import com.imooc.spring.ApplicationTests;
import com.imooc.spring.entity.ProductCategory;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类目仓库测试
 */

public class ProductCategoryRepositoryTest extends ApplicationTests {

    private static final Logger logger = Logger.getLogger(ProductCategoryRepositoryTest.class);

    @Autowired
    private ProductCategoryRepository productCategoryRepository;     // 商品类目仓库

    @Test
    public void findAllTest() {
        productCategoryRepository.findAll();
    }

    @Test
    public void findAllByTypeInTest() {

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
        List<ProductCategory> productCategoryList = productCategoryRepository.findAllByTypeIn(list);

        logger.info("断言");
        assertThat(productCategoryList.size()).isEqualTo(2);
    }
}