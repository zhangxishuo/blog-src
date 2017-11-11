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

# 思路

思路很明确，新建`DingController`控制器`push`方法，当访问该路由时，`push`再去调用`M`层中相应的方法获取需要推送的信息，再调用官方的`request_by_curl`方法实现消息推送。

# 代码实现

## 配置Hook

机器人的`Hook`地址可能会经常改动，所以我们要将其配置在`config.php`中动态获取，方便以后修改。

在`application/config.php`中，添加一个`hook`配置项：

```php
//钉钉机器人Hook地址
'hook' => 'https://oapi.dingtalk.com/robot/send?xxxxxx'
```

## 控制器

```php
<?php
namespace app\index\controller;

use think\Controller;
use app\index\model\Ding;

class DingController extends Controller {

    /**
    * 自动推送钉钉消息方法
    */
    public function push() {

        $ding = new Ding();
        $time = time();
        $time = $time % 86400;

        // 上午
        if ($time <= 600) {
            $message = $ding->getMessage('morning');
            $ding->autoPush($message);
        }
        // 下午
        else if ($time >= 20700 && $time <= 21300) {
            $message = $ding->getMessage('afternoon');
            $ding->autoPush($message);
        }
        // 晚上
        else if ($time >= 35700 && $time <= 36300) {
            $message = $ding->getMessage('night');
            $ding->autoPush($message);
        }
    }
}
```

## 模型层

```php
<?php

namespace app\index\model;

use app\index\model\Term;
use app\index\model\User;
use app\index\model\Week;

/**
 * 张喜硕
 * 钉钉推送类
 */

class Ding {

    /**
     * 钉钉Hook推送消息方法
     */
    public function autoPush($message) {

        $webhook = config('hook');

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

    /**
     * 官方提供的推送方法
     */
    public function request_by_curl($remote_server, $post_string) {

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

    /**
    * 根据时间获取相应消息
    */
    public function getMessage($timeMsg) {

        $knobs = [
            '[第一节]',
            '[第二节]',
            '[第三节]',
            '[第四节]',
            '[第五节]'
        ];

        $users = User::getUsualUsers();
        $term  = Term::getCurrentTerm();
        $week  = new Week();
        $time  = strtotime($term->start_time);

        $current_day  = User::getDay();
        $current_week = $week->WeekDay($time, time());

        foreach ($users as $key => $user) {
            $user->term = $term->id;
            $user->day  = $current_day;
        }

        $messages = [];

        if ($timeMsg == 'morning') {

            $messages = $this->putMessage($messages, $knobs, $users, $current_week, 1);
            $messages = $this->putMessage($messages, $knobs, $users, $current_week, 2);
        } else if ($timeMsg == 'afternoon') {

            $messages = $this->putMessage($messages, $knobs, $users, $current_week, 3);
            $messages = $this->putMessage($messages, $knobs, $users, $current_week, 4);
        } else if ($timeMsg == 'night') {

            $messages = $this->putMessage($messages, $knobs, $users, $current_week, 5);
        }

        $dingMsg = "";

        foreach ($messages as $key => $message) {

            $dingMsg = $dingMsg . $message . "\n";
        }

        return $dingMsg;
    }

    /**
    * 拼接用户课程状态字符串
    */
    public function putMessage($messages, $knobs, $users, $week, $knob) {

        array_push($messages, $knobs[$knob - 1]);

        foreach ($users as $key => $user) {
            
            $message = $this->getState($user, $week, $knob);
            $message = $this->dataFormat($key, $user, $message);

            array_push($messages, $message);
        }

        return $messages;
    }

    /**
    * 获取用户状态信息
    */
    public function getState($user, $week, $knob) {

        $user->knob = $knob;

        $state = $user->CheckedState($week);

        switch ($state) {
            case 1:
                $message = '请假';
                break;
            
            case 2:
                $message = '有课';
                break;

            case 3:
                $message = '加班';
                break;

            case 4:
                $message = '休息';
                break;

            case 5:
                $message = '无课';
                break;

            case 6:
                $message = '缺班';
                break;
        }

        return $message;
    }

    /**
    * 格式化数据
    */
    public function dataFormat($key, $user, $message) {

        $temp = '' . $key + 1 . '.' . $user->name . ' ' . $message;
        return $temp;
    }

}
```

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