---
title: PHP判断对象相等
date: 2017-09-24 15:32:47
categories: THINK DIFFERENT
tags:
- PHP
- 比较
---
执行更新操作时，接收前台传来的数据，我们如果将原对象从数据库中取出来，如果内容没有改变，存储时会发生错误，所以我们通常要对传入的数据与原数据进行比较。

<!--more-->

# 原始方案

更新景点，原代码是直接将传来的数据与取出的数据进行比较。

```php
$message = [];

$attractionId = $param->param('attractionId');
$Attraction   = Attraction::get($attractionId);

$trip  = $param->post('trip');
$date  = $param->post('date');
$guide = $param->post('guide');
$meals = $param->post('meal/a');
$car   = $param->post('car');
$articleId   = $param->param('articleId/d');
$hotelId     = $param->post('hotelId/d');
$description = $param->post('description');

if ($Attraction->trip == $trip && $Attraction->date == $date && $Attraction->guide == $guide && $Attraction->meal == json_encode($meals) && $Attraction->car == $car && $Attraction->article_id == $articleId && $Attraction->hotel_id == $hotelId && $Attraction == $description) {
    $message['status']  = 'error';
    $message['message'] = '信息未更改';
    return $message;
} else {
    $Attraction->trip  = $trip;
    $Attraction->date  = $date;
    $Attraction->guide = $guide;
    $Attraction->meal  = json_encode($meals);
    $Attraction->car   = $car;
    $Attraction->article_id  = $articleId;
    $Attraction->hotel_id    = $hotelId;
    $Attraction->description = $description;

    if (!$Attraction->save()) {
        $message['status']  = 'error';
        $message['message'] = '更新失败';
        return $message;
    }
}
```

# 分析

虽然代码已经按照很整洁的方式去写了，但是那足够占好几行的一个`if`语句实在是扰乱我们读代码的心情。

同时，写了一行这么长的代码，谁敢保证过程中不出错呢？

# 新的方案

一筹莫展之时，我们想起了之前用到的`json_encode`函数，之前是用该函数将`php`的数组转化为`json`字符串，然后存储到数据库中。

该方法可以将变量转化为字符串，那对象也能转化咯？

让我们试一下，两个相同的对象，一个去接收传过来的数据，另一个用作对比，如果他们转化后的`json`字符串是相同的，就说明没有改变数据，我们就不执行保存操作。

```php
$message = [];

$attractionId = $param->param('attractionId');
$Attraction   = Attraction::get($attractionId);
$ContrastAttraction = clone $Attraction;

$trip  = $param->post('trip');
$date  = $param->post('date');
$guide = $param->post('guide');
$meals = $param->post('meal/a');
$car   = $param->post('car');
$articleId   = $param->param('articleId/d');
$hotelId     = $param->post('hotelId/d');
$description = $param->post('description');

$Attraction->trip  = $trip;
$Attraction->date  = $date;
$Attraction->guide = $guide;
$Attraction->meal  = json_encode($meals);
$Attraction->car   = $car;
$Attraction->article_id  = $articleId;
$Attraction->hotel_id    = $hotelId;
$Attraction->description = $description;

if (json_encode($Attraction) != json_encode($ContrastAttraction)) {
    if (!$Attraction->save()) {
        $message['status']  = 'error';
        $message['message'] = '更新失败';
        return $message;
    }
}
```

怎么样，代码是不是比原来更整洁呢？

# 问题

注意，第五行：`$ContrastAttraction = clone $Attraction`，这里需要用`clone`来生成一个完全相同的对象。

为什么一定要`clone`呢？直接像`$ContrastAttraction = $Attraction`这样赋值不也是一个相同的对象吗？结果究竟如何呢，我们试一试。

## 尝试

我们来写一段代码试验一下。

```php
$Attraction = Attraction::get($attractionId);
$ContrastAttraction = $Attraction;

$testTempOne = (json_encode($Attraction) == json_encode($ContrastAttraction));
$FirstChange = $testTempOne;  // true

$Attraction->trip         = '测试数据';
$ContrastAttraction->trip = '对比数据';

$testTempTwo  = (json_encode($Attraction) == json_encode($ContrastAttraction));
$SecondChange = $testTempTwo;  // true
```

打个断点，我们得到两次的结果`$FirstChange`和`$SecondChange`都是`true`。

第一步是`true`合理，但第二步我们已经将两个对象的`trip`赋为不同的值了，结果还是`true`，说明这个方法是有问题的。

在PHP5中，直接使用等号赋值`$ContrastAttraction = $Attraction`，实现的是对象的引用。

我们定义的`$Attraction`和`$ContrastAttraction`这两个所谓的对象实际上都是两个指针，真正的对象存储在独立的结构`Object Store`中，所以我们改变其中任意一个的值，直接就是改变了`Object Store`中对象的属性，另一个也是该对象的引用，所以两个指针时刻保持相同。

# 总结

1. 多去思考，总会发现一个实现该功能更简单的方法。

2. 直接赋值实现的是对象的引用，想要得到一个独立的相同对象需要使用`clone`方法。

3. `json_encode`可以将PHP变量转化为`json`字符串，利用好这个方法我们可以实现许多简便的操作。