---
title: 云智贡献值管理开发
date: 2018-02-06 17:19:50
categories: 智能
tags:
- php
- github
- 接口
---

2018，团队引入了贡献值制度，根据`Github`上的`Pull Request`情况来进行贡献值累计，很荣幸担任这个功能的开发者。

{% asset_img 0.png %}

<!-- more -->

# 开发思路

## 功能分析

能从`Github`上相应的`Pull Request`消息中获取对应成员的贡献值，并累计。

能对贡献值进行管理，基本的`CRUD`。

按时推送到移动端，能方便地查看贡献值。
