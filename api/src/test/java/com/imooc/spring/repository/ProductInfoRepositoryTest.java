package com.imooc.spring.repository;

import com.imooc.spring.ApplicationTests;
import com.imooc.spring.entity.ProductInfo;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品信息仓库测试
 */

public class ProductInfoRepositoryTest extends ApplicationTests {

    private static final Logger logger = Logger.getLogger(ProductInfoRepositoryTest.class);

    @Autowired
    private ProductInfoRepository productInfoRepository;      // 商品信息仓库

    @Test
    public void findAllTest() {
        productInfoRepository.findAll();
    }

    @Test
    public void findAllByStatusTest() {

        logger.debug("新建并保存正常商品");
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setId("123456");
        productInfo1.setStatus(ProductInfo.STATUS_NORMAL);
        productInfoRepository.save(productInfo1);

        logger.debug("新建并保存下架商品");
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setId("456789");
        productInfo2.setStatus(ProductInfo.STATUS_STOP);
        productInfoRepository.save(productInfo2);

        logger.debug("查询并断言");
        List<ProductInfo> productInfoList = productInfoRepository.findAllByStatus(ProductInfo.STATUS_NORMAL);
        assertThat(productInfoList.size()).isEqualTo(1);
    }
}