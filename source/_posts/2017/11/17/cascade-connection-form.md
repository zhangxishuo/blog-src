---
title: 级联表单实现
date: 2017-11-17 22:39:24
categories: 技术
tags:
- JavaScript
---
用户提了一个非常考验`JavaScript`基础的需求————级联表单。

为了实现该功能，抱着自己简单掌握的一些`JavaScript`基础去写代码，最后实现如下效果。

![](/images/2017/11/
17/cascade-connection-form/0.png)
<!-- more -->
# 分析

## 问题描述

根据`ER`图设计的关系，一个酒店实体含有名称，国家，城市，星级，备注等信息。

![](/images/2017/11/
17/cascade-connection-form/1.png)

用户的需求就是能够根据国家、城市、星级实现动态查找，又称为“级联表单”。

## 整体思想

四个`select`下拉框，分别表示国家、城市、星级、名称。

先不考虑数据如何获取，首先先写出基本的`html`代码：

```html
<div class="form-group">
    <label>酒店</label>
    <div class="row">
        <div class="col-md-2">
            <select id="hotel-country" class="form-control">

            </select>
        </div>
        <div class="col-md-2">
            <select id="hotel-city" class="form-control">

            </select>
        </div>
        <div class="col-md-2">
            <select id="hotel-star" class="form-control">

            </select>
        </div>
        <div class="col-md-2">
            <select name="hotelId" id="hotel-name" class="form-control">

            </select>
        </div>
    </div>
</div>
```

第一个下拉框循环国家，然后城市根据选中的国家从后台获取，星级再根据选中的城市获取，以此类推，循环出所有匹配的酒店。

编辑时，比较各循环参数与`hotel`的属性，如相同则设置默认选中。

# 实现