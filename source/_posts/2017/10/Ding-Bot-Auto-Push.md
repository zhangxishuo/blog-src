---
title: 钉钉设置自动推送消息机器人
date: 2017-10-29 17:21:08
categories: 技术
tags:
- 钉钉
- shell
- php
---
之前做的课程管理系统，作为第一个项目，现在回去看自己之前写的代码实在是不敢恭维。

每次想查看每个人的有课无课情况都需要去打开网址登录查看，实在过于麻烦。

正好，钉钉群中每天为我们推送消息的`Github`机器人给了我们灵感，我们可不可以自定义一个自动推送消息的机器人放在钉钉中呢？

<!-- more -->

# 官方文档

钉钉的开放平台写的很详细，[钉钉|开放平台](https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.zZIvnt&treeId=257&articleId=105735&docType=1)

读一遍官方的`Demo`，感觉对整个流程有一个非常清晰的认识。

![](/images/2017/10/Ding-Bot-Auto-Push/0.png)

每个机器人都有一个特定的`Hook`地址，其实只是一个形象的名字，其实就是我们每天都能见到的`url`，在这个`url`上以`POST`形式发送数据，就会在钉钉群中发消息。

# 请求方法

```php
function request_by_curl($remote_server, $post_string) {  
    $ch = curl_init();  
    curl_setopt($ch, CURLOPT_URL, $remote_server);
    curl_setopt($ch, CURLOPT_POST, 1); 
    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 5); 
    curl_setopt($ch, CURLOPT_HTTPHEADER, array ('Content-Type: application/json;charset=utf-8'));
    curl_setopt($ch, CURLOPT_POSTFIELDS, $post_string);  
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);  
    $data = curl_exec($ch);
    curl_close($ch);  
               
    return $data;  
}  
```

本来以为会很复杂，其实官网已经把方法都给我们写好了，我们只需要使用官方给我们提供的`request_by_curl`方法，第一个参数为机器人的`HOOK`地址，第二个参数为我们需要传过去的消息。

本文只是介绍如何使用钉钉自动推送消息，而在某些系统中具体获取数据的方法不一样，这里不具体讲解。

# Shell

我们的课表系统是使用`ThinkPHP`开发的，我们建立一个`DingController`