package com.mengyunzhi.SpringBoot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                   // 商品类目id

    private String categoryName;       // 类目名称

    private Integer categoryType;      // 类目编号

    @CreationTimestamp
    private Date createTime;           // 创建时间

    @UpdateTimestamp
    private Date updateTime;           // 更新时间
}
