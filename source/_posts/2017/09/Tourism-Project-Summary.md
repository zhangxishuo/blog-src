---
title: 旅游项目总结
date: 2017-09-30 10:28:57
categories: 总结
tags:
- ThinkPHP
- 总结
---
从接项目，设计原型，设计ER图，建立数据库，开始写代码；到客户修改需求，代码重构，再到目前的上线测试，经历了一个月的时间。

也算是经历的第一个完整的项目。

<!-- more -->

# 优秀代码

经历了客户的改需求之后，才懂得写出好代码是多么重要。

- 一流程序员的代码能让别人看懂
- 二流程序员的代码能让自己看懂
- 三流程序员的代码能让编译器看懂

客户的需求总是会变的，我们写出好代码的目的就是为了当客户改需求时，我们要修改的代码条理清晰，能大大提高效率。

# 注释

基本的代码人人都能看懂，就没有去写注释，只是在一些含有循环等复杂语句时才会去写一行注释。

- 对于类，应包含简单的描述、作者以及最近的更改日期
- 对于方法，应包含目的的描述、功能、参数以及返回值

```php
public function saveHotel($param) {

    $message = [];
    $message['message'] = '保存成功';
    $message['status']  = 'success';

    $hotel = new Hotel();
    $hotel->designation = $param->post('designation');
    $hotel->city = $param->post('city');
    $hotel->country = $param->post('country');
    $hotel->star_level = $param->post('star_level');
    $hotel->remark = $param->post('remark');

    if(!$hotel->validate(true)->save()) {
        $message['message'] = $hotel->getError();
        $message['status']  = 'error';
    }

    return $message;
}
```
