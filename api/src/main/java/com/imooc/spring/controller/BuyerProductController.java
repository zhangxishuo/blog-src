package com.imooc.spring.controller;

import com.imooc.spring.entity.ProductCategory;
import com.imooc.spring.entity.ProductInfo;
import com.imooc.spring.service.ProductCategoryService;
import com.imooc.spring.service.ProductInfoService;
import com.imooc.spring.utils.ResultViewUtil;
import com.imooc.spring.view.ProductInfoView;
import com.imooc.spring.view.ProductView;
import com.imooc.spring.view.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxishuo on 2018/4/22
 * 买家商品
 */

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;           // 商品信息

    @Autowired
    private ProductCategoryService productCategoryService;   // 商品类目

    @GetMapping("/list")
    public Result list() {

        // 1.查询所有的上架商品
        List<ProductInfo> productInfoList = productInfoService.findAllUp();

        // 2.查询类目(一次性查询)
        List<Integer> categoryTypeList = productInfoList.stream()
                                            .map(e -> e.getCategoryType())
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

        return ResultViewUtil.success(productViewList);
    }
}
