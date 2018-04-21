package com.imooc.spring.service;

import com.imooc.spring.ApplicationTests;
import com.imooc.spring.entity.ProductInfo;
import com.imooc.spring.repository.ProductInfoRepository;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangxishuo on 2018/4/21
 * 商品信息服务实现类单元测试
 */

public class ProductInfoServiceImplTest extends ApplicationTests {

    private static final String id = "123456";               // 通用id

    private static final Logger logger = Logger.getLogger(ProductInfoServiceImplTest.class);

    @Autowired
    private ProductInfoRepository productInfoRepository;     // 商品信息仓库

    @Autowired
    private ProductInfoService productInfoService;           // 商品信息服务

    @Test
    public void findOneTest() {

        logger.debug("新建商品并保存");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(id);
        productInfoRepository.save(productInfo);

        logger.debug("查询并断言");
        ProductInfo newProductInfo = productInfoService.findOne(id);
        assertThat(newProductInfo.getId()).isEqualTo(id);
    }

    @Test
    public void findAllUpTest() {

        logger.debug("新建商品并保存");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(id);
        productInfo.setStatus(ProductInfo.STATUS_NORMAL);
        productInfoRepository.save(productInfo);

        logger.debug("查询并断言");
        List<ProductInfo> productInfoList = productInfoService.findAllUp();
        assertThat(productInfoList.size()).isNotZero();
    }

    @Test
    public void pageAllTest() {

        logger.debug("新建商品并保存");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(id);
        productInfoRepository.save(productInfo);

        logger.debug("构造分页信息");
        PageRequest pageRequest = new PageRequest(0, 2);

        logger.debug("查询并断言");
        Page<ProductInfo> productInfoPage = productInfoService.pageAll(pageRequest);
        assertThat(productInfoPage.getTotalElements()).isNotZero();
    }

    @Test
    public void saveTest() {

        logger.debug("新建商品并保存");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(id);

        logger.debug("保存");
        productInfoService.save(productInfo);

        logger.debug("查询并断言");
        ProductInfo myProductInfo = productInfoRepository.findOne(productInfo.getId());
        assertThat(myProductInfo).isNotNull();
    }
}