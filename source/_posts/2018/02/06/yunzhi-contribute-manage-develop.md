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

1. 能从`Github`上相应的`Pull Request`消息中获取对应成员的贡献值，并累计。

2. 能对贡献值进行管理，基本的`CRUD`。

3. 按时推送到移动端，能方便地查看贡献值。

4. 贡献值公开，开发移动端界面，方便查询。

# Github接口

## 数据推送

`Github`上，每一个仓库都有一个`Webhooks`的配置。

{% asset_img 1.png %}

`Webhooks`，即`Web`钩子，当有特定事件发生时，`Github`就会推送消息。

这里我们填写一个钉钉机器人的`hook`地址，作为测试，我们可以查看每次发生消息时`Github`推送出来的消息，我们复制一份`json`数据作为测试。

## 逻辑算法

当操作`Pull Request`时，`(Open/Close)`，会推送消息。

1. 代码是否合并

2. 获取是谁提交的代码

3. 获取本次提交的贡献度

4. 累加贡献度

## 功能实现

```php
/**
 * 解析Github数据的接口
 * zhangxishuo
 */
public function interface() {
    $json = file_get_contents('php://input');            // 获取传来的json数据
    Contribution::count($json);                          // 调用count方法计数
}
```

这里我们使用`file_get_contents`函数获取`Github`推送来的数据，虽然都是`Post`，但是我们没有使用`ThinkPHP`中为我们提供的`Request`实例中的`post`方法。

本篇文章主要讲解功能开发，具体函数之间的区别请参考这篇文章，[123]()。
