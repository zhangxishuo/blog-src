---
title: PHP获取数据 INPUT VS $_POST
date: 2018-02-09 23:31:51
categories: 探究
tags:
- input
- post
- php
---

开发`Github`接口时，我们使用`php://input`获取`json`数据，而没有使用我们之前一直用的`Request`实例中的`post`方法。我们一起来探究一下区别。

{% asset_img 0.png %}

<!-- more -->

# INPUT

> php://input 是个可以访问请求的原始数据的只读流。 enctype="multipart/form-data" 的时候 php://input 是无效的。

也就是说，`php://input`无法获取`Content-Type`为`multipart/form-data`的数据。

# Request

打开`think\Request.php`文件，我们查看一下`post`方法的源代码。

```php
/**
 * 设置获取获取POST参数
 * @access public
 * @param string        $name 变量名
 * @param mixed         $default 默认值
 * @param string|array  $filter 过滤方法
 * @return mixed
 */
public function post($name = '', $default = null, $filter = null) {
    if (empty($this->post)) {
        $this->post = $_POST;
    }
    if (is_array($name)) {
        $this->param       = [];
        return $this->post = array_merge($this->post, $name);
    }
    return $this->input($this->post, $name, $default, $filter);
}
```

我们看到，框架为我们提供的`post`方法不过是`php`原生的`$_POST`的加强版，可以获取一个数组，可以添加默认值与过滤方法。

# $_POST

> 当 HTTP POST 请求的 Content-Type 是 application/x-www-form-urlencoded 或 multipart/form-data 时，会将变量以关联数组形式传入当前脚本。

这两个`Content-Type`我们熟悉，当表单提交普通数据时，我们会使用`application/x-www-form-urlencoded`。

当表单需要提交如文件等时，我们会使用`multipart/form-data`。

# 使用

所以，我们在使用表单时，通常使用`$_POST`获取数据。

而在为`Github`开发接口时，`Github`为我们推送的数据为`json`格式，其头信息`Content-Type`为`application/json`，所以需要使用`php://input`输入流来获取数据。
