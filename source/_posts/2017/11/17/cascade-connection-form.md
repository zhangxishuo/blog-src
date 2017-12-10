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

## HTML

我们先给`select`框添加点击事件，`$hotel`对象中，我们自定义了`getAllCountries()`方法，获取所有国家，我们将国家循环，当国家名相同时，国家框选中。

```html
<div class="row">
    <div class="col-md-2">
        <select id="hotel-country" class="form-control" onclick="hotelGetCity()">
            {volist name="$hotel->getAllCountries()" id="country"}
            <option {eq name="$hotel->country" value="$country"}selected="true"{/eq} >
                {$country}
            </option>
            {/volist}
        </select>
    </div>
    <div class="col-md-2">
        <select id="hotel-city" class="form-control" onclick="hotelGetStar()">

        </select>
    </div>
    <div class="col-md-2">
        <select id="hotel-star" class="form-control" onclick="hotelGetName()">

        </select>
    </div>
    <div class="col-md-2">
        <select name="hotelId" id="hotel-name" class="form-control">

        </select>
    </div>
</div>
```

## JavaScript

基础界面写好了，我们使用`JavaScript`从后台获取数据，并在`select`中动态添加`option`。

以`hotelGetCity()`为例，点击国家或页面加载完成后执行该方法，获取城市`select`节点，同时清空城市节点中的元素，最后根据选中的国家向后台发送`ajax`请求，成功，则创建在城市`select`节点中创建`option`元素，如果与传过来的`$hotel`中的`city`属性相同，则设置选中，同时自动调用下一个节点的获取方法。

```javascript
function hotelGetCity() {
    /* 获取数据 */
    var city         = document.getElementById('city').innerHTML;
    var countryNode  = document.getElementById('hotel-country');
    var cityNode     = document.getElementById('hotel-city');

    /* 清空 */
    clear(cityNode);

    /* 请求 */
    var countryIndex = countryNode.selectedIndex;
    var url          = "/index/attraction/getCity?country=" + countryIndex;
    ajaxGet(url, function (response) {
        var cities = response;
        createOption(cityNode, cities);
        setSelected(cityNode, city);
        hotelGetStar();
    });
}

function hotelGetStar() {
    /* 获取数据 */
    var star         = document.getElementById('star').innerHTML;
    var countryNode  = document.getElementById('hotel-country');
    var cityNode     = document.getElementById('hotel-city');
    var starNode     = document.getElementById('hotel-star');

    /* 清空 */
    clear(starNode);

    /* 请求 */
    var countryIndex = countryNode.selectedIndex;
    var cityIndex    = cityNode.selectedIndex;
    var url          = "/index/attraction/getStar?country=" + countryIndex + "&city=" + cityIndex;
    ajaxGet(url, function (response) {
        var stars = response;
        createOption(starNode, stars);
        setSelected(starNode, star);
        hotelGetName();
    });
}

function hotelGetName() {
    /* 获取数据 */
    var name         = document.getElementById('name').innerHTML;
    var countryNode  = document.getElementById('hotel-country');
    var starNode     = document.getElementById('hotel-star');
    var cityNode     = document.getElementById('hotel-city');
    var nameNode     = document.getElementById('hotel-name');

    /* 清空 */
    clear(nameNode);

    /* 请求 */
    var countryIndex = countryNode.selectedIndex;
    var cityIndex    = cityNode.selectedIndex;
    var starIndex    = starNode.selectedIndex;
    var url          = "/index/attraction/getHotelName?country=" + countryIndex + "&city=" + cityIndex + "&star=" + starIndex;
    ajaxGet(url, function (response) {
        var names  = response;
        var inners = [];
        var values = [];

        for (var i = 0; i < names.length; i ++) {
            inners.push(names[i].designation);
            values.push(names[i].id);
        }

        createOption(nameNode, inners, values);
        setSelected(nameNode, name);
    }); 
}

/* 清空节点 */
function clear(node) {
    node.length = 0;
}

/* 设置选中 */
function setSelected(node, value) {
    for (var i = 0; i < node.options.length; i ++) {
        if (node.options[i].innerHTML == value) {
            node.options[i].setAttribute("selected", "true");
        }
    }
}

/* 创建option元素，values可选 */
function createOption(node, inners, values) {
    for (var i = 0; i < inners.length; i ++) {
        var option = document.createElement('option');

        if (arguments.length == 3) {
            option.value = values[i];
        }

        option.innerHTML = inners[i];
        node.appendChild(option);
    }
}

/* 对指定url发送get类型的ajax请求 */
function ajaxGet(url, success) {

    var host = window.location.host;
    var url  = "http://" + host + url;

    $.ajax({
        url:   url,
        type: "get",
        success: function (response) {
            success(response);
        },
        error: function (xhr) {
            console.log('server error');
        }
    });
}

/* 页面加载完成后，即执行hotelGetCity()方法 */
window.onload = function () {
    hotelGetCity();
}
```

## 后台

```php
/* AttractionController.php */

public function getCity() {
    $countryIndex = Request::instance()->param('country');

    $hotel     = new Hotel();
    $countries = $hotel->getAllCountries();
    $cities    = $hotel->getCitiesByCountry($countries[$countryIndex]);

    return $cities;
}

public function getStar() {
    $countryIndex = Request::instance()->param('country');
    $cityIndex    = Request::instance()->param('city');

    $hotel     = new Hotel();
    $countries = $hotel->getAllCountries();
    $cities    = $hotel->getCitiesByCountry($countries[$countryIndex]);
    $stars     = $hotel->getStarsByCountryAndCity($countries[$countryIndex], $cities[$cityIndex]);

    return $stars;
}

public function getHotelName() {
    $countryIndex = Request::instance()->param('country');
    $cityIndex    = Request::instance()->param('city');
    $starIndex    = Request::instance()->param('star');

    $hotel     = new Hotel();
    $countries = $hotel->getAllCountries();
    $cities    = $hotel->getCitiesByCountry($countries[$countryIndex]);
    $stars     = $hotel->getStarsByCountryAndCity($countries[$countryIndex], $cities[$cityIndex]);
    $names     = $hotel->getHotelsByCountryAndCityAndStar($countries[$countryIndex], $cities[$cityIndex], $stars[$starIndex]);

    return $names;
}
```

```php
/* Hotel.php */

class Hotel extends Model {

    public function getAllCountries() {
        $countries = [];
        $hotels    = self::all();
        if (!is_null($hotels)) {
            foreach ($hotels as $hotel) {
                array_push($countries, $hotel->country);
            }
        }

        return $this->reIndex($countries);
    }

    public function getCitiesByCountry($country) {
        $cities = [];
        $hotels = self::where('country', '=', $country)->select();
        if (!is_null($hotels)) {
            foreach ($hotels as $hotel) {
                array_push($cities, $hotel->city);
            }
        }
        
	return $this->reIndex($cities);
    }

    public function getStarsByCountryAndCity($country, $city) {
        $stars = [];
        $map   = [];
        $map['country'] = $country;
        $map['city']    = $city;
        $hotels = self::where($map)->select();
        if(!is_null($hotels)) {
            foreach ($hotels as $hotel) {
                array_push($stars, $hotel->star_level);
            }
        }
        
	return $this->reIndex($stars);
    }

    public function getHotelsByCountryAndCityAndStar($country, $city, $star) {
        $map               = [];
        $map['country']    = $country;
        $map['city']       = $city;
        $map['star_level'] = $star;
        $hotels = self::where($map)->select();
        return $hotels;
    }

    public function reIndex($array) {
	$temps = array_unique($array);
	$array = [];

        foreach ($temps as $temp) {
            array_push($array, $temp);
        }

	return $array;
    }
}
```

# 问题

## 索引为-1

当时实现编辑功能时，遇到的一个最大的问题就是获取上一级选中的索引时，`SelectedIndex`一直为`-1`，后来通过控制台打印才发现问题。

最开始，我的想法是这样的。

```javascript
/* 页面加载完成后，即执行hotelGetCity()方法 */
window.onload = function () {
    hotelGetCity();
    hotelGetStar();
    hotelGetName();
}
```

页面加载完之后，依次执行三个方法，实现请求并默认选中的功能。

但是却忽略了一个`JavaScript`的特性————异步。

通常的网页由`HTML`、`CSS`、`JavaScript`编写而成，浏览器渲染`HTML`与`CSS`的速度是非常快的，浏览器的速度差异就在于解析`JavaScript`的速度。所以通常大家说的`Chrome`和`Safari`快在于其解析`JavaScript`的速度快。

`ajax`，即`Asynchronous JavaScript And XML`，异步的`JavaScript`和`XML`。

当请求资源的时候，为了不影响用户体验，我们不会去等待资源返回之后再去向下运行，而是你请求就去请求，我继续执行代码，当你资源回来，我再去操作。

所以当请求城市时，还没有得到返回值，就去执行获取星级的方法，而这是城市的获取还没有完成，即没有执行创建`option`及设置选中的操作，所以`select`框这时是空的，所以索引为`-1`。

# 总结

实现级联表单的编辑中，忽略了`JavaScript`的异步特性，导致浪费了较多时间。

写`JavaScript`的代码时心里要时刻记得异步特性。