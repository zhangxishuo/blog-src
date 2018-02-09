---
title: 深入理解回调函数
date: 2018-01-06 17:11:36
categories: 学习
tags:
- callback
- javascript
---

在使用`javascript`开发中，回调函数经常被使用。

{% asset_img 0.png %}

起初，我们只是惊叹于`javascript`的魅力，可以将一个函数作为参数传进去，但是对于深层的知识以及设计模式不甚了解，希望本文能对你在回调函数的认识上有所帮助。

<!-- more -->

# ***认识***

## ***概念***

> 在计算机程序设计中，回调函数，或简称回调(`callback`)，是指通过函数参数传递到其它代码的，某一块可执行代码的引用。这一设计允许了底层代码调用在高层定义的子程序。

这是维基百科上的专业概念。

高深的定义并不适合我们学习，如何不喜欢这个官方的解释，请看下文我个人的理解。

## ***理解***

回调函数通俗一点的理解就是我调用了一个函数，该函数的参数是函数类型，所以我调用该函数时需要传入一个函数进去，而我传入的函数，就称为回调函数。

为什么称为回调呢？`call`：调用，`callback`：回调。如果你想象力丰富的话看到这两个单词应该就明白什么是回调了。

我调用(`call`)了一个函数，而该函数又反过来调用(`callback`)我传给它的这个函数，这个往回调用的过程称为回调。

```javascript
var demo = function (callback) {
  callback();
};
```

就像上面的代码一样，别人写了一个`demo`函数，它的参数中需要一个函数，所以如果我需要使用`demo`函数的话，就需要传一个回调函数进去

```javascript
demo(function () {
  console.log('This is callback');
});
```

在`javascript`的世界里，函数就是对象，可以赋值给变量，也可以作为参数传递。

所以我们传递一个函数作为另一个函数的参数，就像我们传一个对象进去一样平常。

而我们看英文，`call`关键字相信大家都不陌生，调用，从A函数调用B函数，`callback`，即回调，从B函数往回调用A传来的函数。

# ***深入理解***

## ***示例代码***

这是我们在`AngularJS`中使用`$http`进行资源请求的源代码。

```javascript
var self  = this;

self.init = function() {
  var id  = $stateParams.id;                              // 获取url中的id值
  var url = 'http://127.0.0.1:8080/Teacher/' + id;        // 拼接后台url
  $http.get(url)                                          // 发起get请求
    .then(function success(response) {
            $scope.data = response.data;
          }, function error(response) {
            console.log(url + 'error');
            console.log(response);
          });
};

self.init();
```

## ***为什么要使用回调函数？***
