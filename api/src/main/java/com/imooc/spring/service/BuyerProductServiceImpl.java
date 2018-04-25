package com.imooc.spring.service;

import com.imooc.spring.entity.ProductCategory;
import com.imooc.spring.entity.ProductInfo;
import com.imooc.spring.view.ProductInfoView;
import com.imooc.spring.view.ProductView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxishuo on 2018/4/25
 * 买家商品服务实现类
 */

@Service
public class BuyerProductServiceImpl implements BuyerProductService {

    @Autowired
    private ProductInfoService productInfoService;           // 商品信息

    @Autowired
    private ProductCategoryService productCategoryService;   // 商品类目

    @Override
    public List<ProductView> getAllProductView() {
        // 1.查询所有的上架商品
        List<ProductInfo> productInfoList = productInfoService.findAllUp();

        // 2.查询类目(一次性查询)
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryService.findByTypeIn(categoryTypeList);

        // 3.数据拼装
        List<ProductView> productViewList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductView productView = new ProductView();
            productView.setCategoryName(productCategory.getName());
            productView.setCategoryType(productCategory.getType());

            // 遍历商品列表
            List<ProductInfoView> productInfoViewList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getType())) {
                    ProductInfoView productInfoView = new ProductInfoView();
                    // 拷贝属性
                    BeanUtils.copyProperties(productInfo, productInfoView);
                    productInfoViewList.add(productInfoView);
                }
            }

            productView.setProductInfoViewList(productInfoViewList);

            productViewList.add(productView);
        }
        return productViewList;
    }
}
