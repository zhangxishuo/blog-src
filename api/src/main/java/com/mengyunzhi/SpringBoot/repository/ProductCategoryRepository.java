package com.mengyunzhi.SpringBoot.repository;

import com.mengyunzhi.SpringBoot.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
