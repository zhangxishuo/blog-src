package com.imooc.spring.controller;

import com.imooc.spring.service.BuyerProductService;
import com.imooc.spring.utils.ResultViewUtil;
import com.imooc.spring.view.ProductView;
import com.imooc.spring.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangxishuo on 2018/4/22
 * 买家商品
 */

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private BuyerProductService buyerProductService;          // 买家商品服务

    @GetMapping("/list")
    public Result list() {

        List<ProductView> productViewList = buyerProductService.getAllProductView();
        return ResultViewUtil.success(productViewList);
    }
}
