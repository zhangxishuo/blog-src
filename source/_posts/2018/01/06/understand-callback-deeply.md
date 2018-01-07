---
title: 深入理解回调函数
date: 2018-01-06 17:11:36
categories: 学习
tags:
- callback
- javascript
---

在使用`javascript`开发中，回调函数经常被使用。

起初，我们只是惊叹于`javascript`的魅力，可以将一个函数作为参数传进去，但是对于深层的知识以及设计模式不甚了解，希望本文能对你在回调函数的认识上有所帮助。

<!-- more -->

# **认识**

## **概念**

在计算机程序设计中，回调函数，或简称回调(`callback`)，是指通过函数参数传递到其它代码的，某一块可执行代码的引用。这一设计允许了底层代码调用在高层定义的子程序。

这是维基百科上的专业概念。

我唯一的疑问就是为什么这么有意思的东西被定义的如此枯燥？这么枯燥的一个定义谁会感兴趣？如果你和我一样不喜欢这些解释的话，请跳过这里的概念，看看我对回调函数的理解。

## **理解**

在`javascript`的世界里，函数就是对象，可以赋值给变量，也可以作为参数传递。

所以我们传递一个函数作为另一个函数的参数。

而我们看英文，`call`关键字相信大家都不陌生，调用，从A函数调用B函数，`callback`，即回调，从B函数往回调用A传来的函数。

# **深入理解**

## **示例代码**

这是我们在`AngularJS`中使用`$http`进行资源请求的源代码。

```javascript
var self  = this;

self.init = function() {
  var id  = $stateParams.id;
  var url = 'http://127.0.0.1:8080/Teacher/' + id;
  $http.get(url)
    .then(function success(response) {
      $scope.data = response.data;
    }, function error(response) {
      console.log(url + 'error');
      console.log(response);
    });
};

self.init();
```

## **为什么要使用回调函数？**
