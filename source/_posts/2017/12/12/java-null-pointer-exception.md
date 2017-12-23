---
title: 空指针异常
date: 2017-12-12 08:47:50
categories: 学习
tags:
- Java
- 异常
---
写测试代码的时候得到了一个名为`NullPointerException`的异常，译为空指针异常。

`Google`关键字时，发现这是`Java`中比较常见的一个异常，也是正式写`Java`后台遇到的第一个异常。

<!-- more -->

# 问题描述

```java
mandatoryInstrument.getAccuracy().getValue();
```

获取强检器具中的精度中的值，这条语句造成了空指针异常。

# 异常详解

用`Google`搜索关键字`Java NullPointerException`，我在`Stack OverFlow`上找到了答案。

当声明一个变量时，实际上是创建了一个指向对象的指针，当声明一个基本数据类型`int`的变量时，思考一下代码。

```java
int x;
x = 10;
```

变量`x`是`int`类型，`Java`会为`x`初始化为`0`。当为`x`赋值为`10`时，`10`会被写入`x`的指针指向的存储空间。

但是，当声明引用类型时，会发生不同的状况，参考下面的代码：

```java
Integer num;
num = new Integer(10);
```

第一行声明了一个名为`num`的变量，但是，它并没有一个初始的值。相反，它包含一个指针(因为该类型是引用类型`Integer`)。因为没有声明`num`的指针指向什么，`Java`就让该指针指向`null`，意为“什么都不指向”。

第二行，关键字`new`用来创建一个`Integer`类型的对象，并且指针变量`num`被赋值为这个对象。

当声明了一个变量，但是没有创建对象，异常就发生了。如果试图在创建对象之前引用`num`，就会得到空指针异常。

学习了异常之后，再去解决就容易多了。

```java
mandatoryInstrument.getAccuracy().getValue();
```

这里的错误是，在测试时，强检器具的精度被设置为`null`，在`null`上去调用方法，就会出错，出现空指针异常。

在`php`中也有类似的异常，就像我们在`ThinkPHP`框架中常常遇到的`Trying to get property of non-object`(试图从非对象获取属性)错误。

# 参考文献

[What is a NullPointerException, and how do I fix it? - Stack OverFlow
](https://stackoverflow.com/questions/218384/what-is-a-nullpointerexception-and-how-do-i-fix-it)
