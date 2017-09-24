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

# 原方案

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
$articleId   = $param->param('articleId');
$hotelId     = $param->post('hotelId');
$description = $param->post('description');

if ($Attraction->trip == $trip && $Attraction->date == $date && $Attraction->guide == $guide && $Attraction->meal == json_encode($meals) && $Attraction->car == $car && $Attraction->article_id == $articleId && $Attraction->hotel_id == $hotelId && $Attraction == $description) {
    $message['status'] = 'error';
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
        $message['status'] = 'error';
        $message['message'] = '更新失败';
        return $message;
    }
}
```

# 分析

虽然代码已经按照很整洁的方式去写了，但是那足够占好几行的一个`if`语句实在是扰乱我们读代码的心情。

同时，写了一行这么长的代码，谁敢保证过程中不出错呢？

# 新方案

一筹莫展之时，我们想起了之前用到的`json_encode`函数，之前是用该函数将`php`的数组转化为`json`字符串，然后存储到数据库中。

该方法可以将变量转化为字符串，那对象也能转化咯？

让我们试一下，两个相同的对象，一个去接收传过来的数据，另一个用作对比，如果他们转化后的`json`字符串是相同的，就说明没有改变数据，我们就不执行保存操作。

```php
$message = [];

$attractionId = $param->param('attractionId');
$Attraction   = Attraction::get($attractionId);
$ContrastAttraction = $Attraction;

$trip  = $param->post('trip');
$date  = $param->post('date');
$guide = $param->post('guide');
$meals = $param->post('meal/a');
$car   = $param->post('car');
$articleId   = $param->param('articleId');
$hotelId     = $param->post('hotelId');
$description = $param->post('description');

$Attraction->trip  = $trip;
$Attraction->date  = $date;
$Attraction->guide = $guide;
$Attraction->meal  = json_encode($meals);
$Attraction->car   = $car;
$Attraction->article_id  = $articleId;
$Attraction->hotel_id    = $hotelId;
$Attraction->description = $description;

if(json_encode($Attraction) != json_encode($ContrastAttraction)) {
    if(!$Attraction->save()) {
        $message['status'] = 'error';
        $message['message'] = '更新失败';
        return $message;
    }
}
```

怎么样，代码是不是比原来更整洁呢？

注意，第五行：`$ContrastAttraction = $Attraction`，直接赋值`$ContrastAttraction`才和`$Attraction`是相同的。