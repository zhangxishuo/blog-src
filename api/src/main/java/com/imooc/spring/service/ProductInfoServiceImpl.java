package com.imooc.spring.service;

import com.imooc.spring.entity.ProductInfo;
import com.imooc.spring.repository.ProductInfoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/21
 * 商品信息服务实现类
 */

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    private static final Logger logger = Logger.getLogger(ProductInfoServiceImpl.class);

    @Autowired
    private ProductInfoRepository productInfoRepository;       // 商品信息仓库

    @Override
    public ProductInfo findOne(String id) {
        return productInfoRepository.findOne(id);
    }

    @Override
    public List<ProductInfo> findAllUp() {
        return productInfoRepository.findAllByStatus(ProductInfo.STATUS_NORMAL);
    }

    @Override
    public Page<ProductInfo> pageAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }
}
