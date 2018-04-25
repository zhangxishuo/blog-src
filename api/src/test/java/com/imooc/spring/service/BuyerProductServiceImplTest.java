package com.imooc.spring.service;

import com.imooc.spring.ApplicationTests;
import com.imooc.spring.view.ProductView;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangxishuo on 2018/4/25
 * 买家商品服务实现类单元测试
 */

public class BuyerProductServiceImplTest extends ApplicationTests {

    private static final Logger logger = Logger.getLogger(BuyerProductServiceImplTest.class);  // 日志

    @Autowired
    private BuyerProductService buyerProductService;          // 买家商品服务

    @Test
    public void getAllProductView() {

        logger.debug("查询并断言");
        List<ProductView> productViewList = buyerProductService.getAllProductView();
        assertThat(productViewList).isNotNull();
    }
}