package com.mengyunzhi.SpringBoot.repository;

import com.mengyunzhi.SpringBoot.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
