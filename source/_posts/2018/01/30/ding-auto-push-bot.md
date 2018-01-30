---
title: 钉钉机器人自动推送
date: 2018-01-30 22:16:32
categories: 智能
tags:
- 钉钉
- php
---

[课表管理系统](https://github.com/yunzhiclub/courseManageSystem)，是我们用`php`开发的第一个`web`项目，虽然功能实现上没有太大的问题，但是代码的规范性还是不敢恭维。这个项目或许就这样永远停留在`Github`中，就这样，不去修改，这会警示着我们代码的整洁。

该项目已经上线，在不考虑代码规范性的前提下，系统的功能还是没有问题的。

{% asset_img 0.png %}

这是显示的首页，每节课我们都需要去亲自登录系统去查看每个人的课程状态，过于麻烦。

正好，钉钉群中每天为我们推送消息的`Github`机器人给了我们灵感，我们可不可以自定义一个自动推送消息的机器人放在钉钉中呢？

<!-- more -->

# Demo

## 原理

钉钉的开放平台写的很详细，[钉钉|开放平台](https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.zZIvnt&treeId=257&articleId=105735&docType=1)

读一遍官方的`Demo`，感觉对整个流程有一个非常清晰的认识。

{% asset_img 1.png %}

每个绑定到钉钉群的机器人都有一个`"Hook"`地址，其实就是我们每天都能见到的`url`，我们在这个地址上`post`指定格式的数据，就能让该机器人在钉钉群中发消息。

## 思路

在钉钉群中建立群机器人，获取`"Hook"`地址。

建立相应控制器，相应方法，该方法负责完成一系列获取数据、且向相应`"Hook"`地址`post`数据的操作。

建立脚本，该脚本访问映射到我们上文方法的特定`url`。

设置定时任务，定时执行该脚本，完成自动推送。

# 实现

## 添加机器人

添加机器人，选择自定义机器人。

{% asset_img 2.png %}

{% asset_img 3.png %}

这里我们获取到该机器人的`"Hook"`地址，我们将其记下，后面配置时会用到。

## 配置Hook

为了让我们的系统更加灵活，我们将机器人的`"Hook"`地址配置在`config.php`中，动态获取，方便以后修改。

在`application/config.php`中，添加一个`hook`配置项：

```php
// 钉钉机器人Hook地址
'hook' => 'https://oapi.dingtalk.com/robot/send?xxxxxx'
```

不要觉得这个配置文件有多复杂，其实言其本质，不过是一个`php`数组，这个数组可以在全局使用。

我们想添加一个配置项，不过是在该数组中添加一个元素。

## M层

我们先写`M`层的方法，官方`Demo`中给了一个`request_by_curl`方法，既然有现成的，我们直接拿过来用。

该方法参数一`$remote_server`为机器人的`"Hook"`地址，参数二`$post_string`为我们要推送的格式化数据。

```php
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
```

该方法需要`"Hook"`地址和推送的数据，`"Hook"`地址已经配置好了，接下来我们写方法获取数据。

这里获取数据的逻辑比较复杂，我们将该方法拆分。

> 每个方法，只做一件事！————《代码整洁之道》

初次完成时，`getMessage`方法非常臃肿，这里，我将重构后的代码介绍一下。

`getState`方法用于获取用户在该节课的状态，`putMessage`方法调用`getState`，获取所有正常用户的信息，用`dataFormat`格式化数据，并拼接为数组返回。

`getMessage`方法调用`putMessage`，根据不同的时间节点调用方法，获取信息数组，同时在最后连接为字符串返回。

**注：这里用于连接的`dingMsg`一定是双引号字符串，`php`不会处理单引号字符串，只会替换双引号字符串中的特殊字符(如换行符等)。**

```php
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

    $messages = [
        '请假',
        '有课',
        '加班',
        '休息',
        '无课',
        '缺班'
    ];

    return $messages[$state - 1];
}

/**
* 格式化数据
*/
public function dataFormat($key, $user, $message) {

    $temp = '' . $key + 1 . '.' . $user->name . ' ' . $message;
    return $temp;
}
```

官方的`request_by_curl`需要接收特定格式的`Json`数据，所以我们还需要将`request_by_curl`封装一下，同时完成数据格式转换与数据推送。

```php
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
```

## 控制器

方法写好了，就等着我们的控制器去调用了，新建控制器`DingController`，新建方法`push`。

这里需要早中晚推送消息，所以我们需要判断时间的几点，然后调用方法时传不同的参数。

```php
/**
 * 自动推送钉钉消息方法
 */
public function push() {

    $ding = new Ding();
    $hour = date("H");

    if ($hour >= 7 && $hour <= 9) {

        $message = $ding->getMessage('morning');
        $ding->autoPush($message);
    }
    else if ($hour >= 13 && $hour <= 15) {

        $message = $ding->getMessage('afternoon');
        $ding->autoPush($message);
    }
    else if ($hour >= 17 && $hour <= 19) {

        $message = $ding->getMessage('night');
        $ding->autoPush($message);
    }
}
```

这里的`date`函数根据设置的时区自动将时间戳转化为该时区的时间，`ThinkPHP`框架中，在`config.php`中配置默认时区。

```php
// 默认时区
'default_timezone'       => 'PRC',
```

# 自动化

上文的代码实现已经完成，但是我们触发方法，还需要访问一个类似`/index/ding/push`的路由，我们不可能让人工去检测每天定时访问路由吧？

所以我们想设置一个定时任务，到达特定时间就访问这个路由，实现消息推送。

这里，我们需要两款工具，一个是访问路由的类浏览器工具，另一个是定时任务设置工具。

## CURL

`CURL`是一个命令行浏览器工具，例如，我们在命令行中利用`CURL`访问`www.baidu.com`，就会给我们打印出百度首页的源代码。

{% asset_img 4.png %}

这个小工具正符合我们的要求，就用它去访问我们需要触发方法的路由。

## SHELL

这里，我们需要写一个`Shell`脚本去完成这个访问路由的操作。

> Shell脚本（英语：Shell script），又称Shell命令稿、程序化脚本，是一种电脑程序与文本文件，内容由一连串的shell命令组成，经由Unix Shell直译其内容后运作。被当成是一种脚本语言来设计，其运作方式与直译语言相当，由Unix shell扮演命令行解释器的角色，在读取shell脚本之后，依序运行其中的shell命令，之后输出结果。利用shell脚本可以进行系统管理，文件操作等。

依据个人理解，`Shell`就是把命令行里执行的命令写在一个文件中去执行。

我可以在命令行中`echo hello world`，也可以把`echo hello world`写在一个`*.sh`格式的脚本里，再去执行这个脚本，这与直接执行命令结果是相同的。

所以，新建一个`ding.sh`文件，写入如下命令：

```bash
curl http://www.test.com/courseManageSystem/public/index.php/index/ding/push
```

我们调整一下系统时间，手动执行一下这个脚本。

```bash
bash ding.sh
```

查看钉钉群，成功。

{% asset_img 5.png %}

## 定时任务

脚本写完了，我们考虑的就是设置一个定时任务，自动执行这个脚本。

`crontab`是`Linux`下的一个定时任务工具，我们可以通过这款工具来实现定时执行脚本的操作。

打开命令行，执行如下命令，编辑定时任务配置项。

```bash
crontab -e
```

{% asset_img 6.png %}

最后几行，就是我们要设置定时任务的配置。

`m(minute)`：分钟`(0 - 59)`

`h(hour)`：小时`(0 - 23)`

`dom(day of month)`：天`(1 - 31)`

`mon(month)`：月`(1 - 12)`

`dow(day of week)`：星期几`(1 - 7)`

`command`：要执行的命令

假设我们要设置每天`8:00`执行该脚本，我们就做如下配置：

```bash
# m h  dom mon dow   command
  0 8   *   *   *    bash /opt/lampp/htdocs/courseManageSystem/ding.sh
```

`Ctrl + X`离开。储存更动过的缓冲区吗？回答`Y`保存。

配置之后，我们可以输入以下命令查看我们配置的定时任务。

```bash
crontab -l
```

# 总结

如此，我们的自动推送机器人就完成了，我们可以直接在服务器中执行脚本，设置定时任务，完成自动推送。

**使用过程中请开启`xampp`等必要的运行环境。**
