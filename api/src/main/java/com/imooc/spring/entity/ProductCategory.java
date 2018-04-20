package com.imooc.spring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * @author zhangxishuo on 2018/4/20
 * 商品类目实体
 */

@Entity
@Data
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;           // 类目id

    private String name;          // 类目名称

    private Integer type;         // 类目编号

    @CreationTimestamp
    private Calendar createTime;  // 创建时间

    @UpdateTimestamp
    private Calendar updateTime;  // 更新时间
}
