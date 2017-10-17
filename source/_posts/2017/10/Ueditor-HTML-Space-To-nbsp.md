---
title: Ueditor空格变成&nbsp;解决方案
date: 2017-10-11 11:32:47
categories: 问题
tags:
- Ueditor
---
为了让代码看起来更整洁，我们往往会在`HTML`中添加部分空格，浏览器会直接忽略空格，不去显示，但是我们引用的富文本编辑器`Ueditor`就不会这样处理了，他会将我们填入的代码空格部分替换为`&nbsp;`，这就会影响我们效果显示，我们的想法是取消`Ueditor`的空格到`&nbsp;`的替换操作。

<!-- more -->

# 问题描述

```html
<ul class=" list-paddingleft-2" style="list-style-type: decimal;">
    <li>
        <p>
            <span class="glyphicon glyphicon-menu-right"></span>      更省心：不用你操心，麻烦的事全都有人包办。
        </p>
    </li>
</ul>
```

好好的`HTML`代码，但是当我们把他粘贴进`Ueditor`的编辑器中，再点击预览，再看代码，空格已经变成`&nbsp;`了。

浏览器会忽略空格，但是被转换后的`&nbsp;`就会正常显示，这就很影响美观了。

```html
<ul class=" list-paddingleft-2" style="list-style-type: decimal;">
    <li>
        <p>
            <span class="glyphicon glyphicon-menu-right"></span> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;更省心：不用你操心，麻烦的事全都有人包办。
        </p>
    </li>
</ul>
```

# 解决方案

我们打开`Ueditor`插件源码：`ueditor.all.js`，我们看到里面有这样一个`isText`方法。

```javascript
function isText(node, arr) {
    if (node.parentNode.tagName == 'pre') {
        //源码模式下输入html标签，不能做转换处理，直接输出
        arr.push(node.data)
    } else {
        arr.push(notTransTagName[node.parentNode.tagName] ? utils.html(node.data) : node.data.replace(/[ ]{2}/g,' &nbsp;'))
    }
}
```

根据注释及名称我们就可以大体理解这个方法，源码模式，如果是在`pre`标签内的代码，不做处理，否则执行`else`内的操作。

我们再看`else`内的代码，`arr.push(notTransTagName[node.parentNode.tagName] ? utils.html(node.data) : node.data.replace(/[ ]{2}/g,' &nbsp;'))`。

如果不是需要转换的标签内容，直接将内容输入，否则，将`/[ ]{2}/g`替换为` &nbsp;`。

我们将最后用于替换的`&nbsp;`改为空，这个问题即完美解决。
