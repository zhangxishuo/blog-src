---
title: 钉钉实现自动推送机器人
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

```php
    public function pushDing($message) {

        $webhook = "欲推送的机器人Hook地址";

        $data = array (
            'msgtype'  => 'text',
            'text'     => array (
                'content' => $message
            )
        );

        $data_string = json_encode($data);

        $result = $this->request_by_curl($webhook, $data_string);

        echo $result;
    }
```

该方法是发送文本格式的信息，同时官方还支持`link`和简易的`markdown`格式。

# 思路

因为每个系统都可能不同，获取信息的方式也不同，这里就不给出本系统获取消息的源代码了。这里给大家介绍一下钉钉机器人自动推送的大体思路。

思路很明确，新建`DingController`控制器`autoPush`方法，当访问该路由时，`autoPush`再去调用`M`层中相应的方法获取需要推送的信息，再调用`pushDing`方法实现消息推送。

# 定时任务

路由不可能我们每天定时`8:00`、`13:50`、`18:00`去手动访问触发推送方法，所以我们想到了定时任务，我们设定一个任务，时间条件满足时就自动访问该路由，实现消息推送。

`Linux`下的`crontab`解决了这个问题：

打开终端：输入命令`crontab -e`即可编辑配置文件，修改我们的定时任务。

![](/images/2017/10/Ding-Bot-Auto-Push/1.png)

这里的最后一行注释给出了我们设置任务的格式，从左到右依次为：

`Minute`：分钟(0 - 59)

`Hour`：小时(0 - 23)

`DayOfMonth`：日期(1 - 31)

`MonthOfYear`：月份(1 - 12)

`DayOfWeek`：星期(1 - 7)

`Command`：要执行的命令