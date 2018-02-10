---
title: 云智贡献值管理开发
date: 2018-02-06 17:19:50
categories: 智能
tags:
- php
- github
- 接口
---

2018，团队引入了贡献值制度，根据`Github`上的`Pull Request`情况来进行贡献值累计，很荣幸担任这个功能的开发者。

{% asset_img 0.png %}

<!-- more -->

# 开发思路

## 功能分析

1. 能从`Github`上相应的`Pull Request`消息中获取对应成员的贡献值，并累计。

2. 能对贡献值进行管理，基本的`CRUD`。

3. 按时推送到移动端，能方便地查看贡献值。

4. 贡献值公开，开发移动端界面，方便查询。

# Github接口

## 数据推送

`Github`上，每一个仓库都有一个`Webhooks`的配置。

{% asset_img 1.png %}

`Webhooks`，即`Web`钩子，当有特定事件发生时，`Github`就会推送消息。

这里我们填写一个钉钉机器人的`hook`地址，作为测试，我们可以查看每次发生消息时`Github`推送出来的消息，我们复制一份`json`数据作为测试。

## 逻辑算法

当操作`Pull Request`时，`(Open/Close)`，会推送消息。

1. 代码是否合并

2. 获取是谁提交的代码

3. 获取本次提交的贡献度

4. 累加贡献度

## 功能实现

```php
/**
 * 解析Github数据的接口
 * zhangxishuo
 */
public function interface() {
    $json = file_get_contents('php://input');            // 获取传来的json数据
    Contribution::count($json);                          // 调用count方法计数
}
```

这里我们使用`file_get_contents`函数获取`Github`推送来的数据，虽然都是`Post`，但是我们没有使用`ThinkPHP`中为我们提供的`Request`实例中的`post`方法。

本篇文章主要讲解功能开发，具体函数之间的区别请参考这篇文章，[PHP获取数据 INPUT VS $_POST](https://zhangxishuo.github.io/2018/02/09/php-get-data-input-vs-post/)。

```php
/**
 * 累计贡献值
 * zhangxishuo
 */
public static function count($json) {
    $data = json_decode($json);                          // json反序列化
    if (self::isMerged($data)) {                         // 如果该提交被合并
        $name = self::getUsername($data);                // 获取提交代码的用户名
        $num  = self::getNum($data);                     // 获取本次贡献值
        self::revise($name, $num);                       // 修改贡献值
    }
}
```

先不去管具体的实现，我们根据逻辑把代码写出来，函数功能之后再去实现。主体逻辑正确，接下来把用到的方法逐个实现。

```php
/**
 * 代码是否合并
 * zhangxishuo
 */
public static function isMerged($data) {
    if ($data->pull_request->merged) {                   // 如果merged属性为true
        return true;                                     // 返回真
    } else {
        return false;                                    // 返回假
    }
}
```

```php
/**
 * 获取用户名
 * zhangxishuo
 */
public static function getUsername($data) {
    $name = $data->pull_request->user->login;            // 获取用户名
    return $name;
}
```

```php
/**
 * 获取提交代码的贡献值
 * zhangxishuo
 */
public static function getNum($data) {
    $message = self::getContributeStr($data);            // 获取贡献值相关字符串
    return self::strFilter($message);                    // 从字符串中过滤出贡献值
}

/**
 * 获取提交中与贡献值相关的字符串
 * zhangxishuo
 */
public static function getContributeStr($data) {
    $message = self::getStr($data);                      // 获取本次合并的所有字符串
    $action  = config('contribute')['action'];           // 获取关键字
    $keyword = config('contribute')['keyword'];          // 获取关键字
    $regular = self::getRegular($action, $keyword);      // 拼接正则表达式
    preg_match($regular, $message, $array);              // 匹配正则
    return $array[0];                                    // 返回匹配的字符串
}

/**
 * 获取本次代码合并时提交的所有字符串
 * zhangxishuo
 */
public static function getStr($data) {
    $url    = $data->repository->commits_url;            // 获取接口格式
    $sha    = $data->pull_request->merge_commit_sha;     // 获取合并时提交的加密码
    $url    = str_replace('{/sha}', '/' . $sha, $url);   // 根据接口格式拼接url
    $header = config('github')['organization'];          // 获取配置中github相关的header信息
    $return = self::curl($url, $header);                 // 访问该url
    $object = json_decode($return);                      // 将结果反序列化
    return $object->commit->message;                     // 返回信息
}

/**
 * 根据header信息访问url
 * zhangxishuo
 */
public static function curl($url, $info) {
    $ch     = curl_init();                               // 初始化
    curl_setopt($ch, CURLOPT_URL, $url);                 // 设置url
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');      // 设置方法为get
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('User-Agent:' . $info));  // 设置header
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);      // 设置将信息以字符串返回
    $return = curl_exec($ch);                            // 执行并获取返回值
    curl_close($ch);                                     // 关闭
    return $return;                                      // 返回
}

/**
 * 根据关键字拼接正则表达式
 * zhangxishuo
 */
public static function getRegular($action, $keyword) {
    /**
     * 拼接正则表达式
     * ([\s]|[^\s])+ : 空白符或非空白符匹配1次以上
     *      i        : 不分区大小写
     */
    $regular = '/' . $action . '([\s]|[^\s])+' . $keyword . '/i';
    return $regular;
}

/**
 * 过滤字符串，获取贡献值
 * zhangxishuo
 */
public static function strFilter($message) {
    $action = config('contribute')['action'];            // 获取关键字
    $length = strlen($message);                          // 获取信息长度
    $actLen = strlen($action);                           // 获取关键字长度
    $subLen = $length - $actLen - 1;                     // 计算截取长度
    $str    = substr($message, $actLen, $subLen);        // 截取数字信息
    return self::numFilter($str);                        // 过滤该数字字符串
}

/**
 * 过滤数字字符串
 * 去空格同时转化为数字
 * zhangxishuo
 */
public static function numFilter($str) {
    $str = trim($str);                                   // 去除前后空格
    return (float) $str;                                 // 转化为浮点型数字
}
```

```php
/**
 * 修改用户的贡献值
 * zhangxishuo
 */
public static function revise($name, $num) {
    $contStatus = self::countContribution($name, $num);  // Contribution表添加数据
    $userStatus = User::addContribution($name, $num);    // User表添加数据
    if ($contStatus && $userStatus) {                    // 都成功
        return true;                                     // 返回真
    }
    return false;                                        // 返回假
}

/**
 * 为用户在Contribution表中保存记录
 * zhangxishuo
 */
public static function countContribution($name, $num) {
    $contribution = model('Contribution');               // 用助手函数实例化一个对象
    $contribution->username = $name;                     // 设置username
    $contribution->state    = $num;                      // 设置state
    try {
        $contribution->save();                           // 保存
    } catch (\Exception $e) {
        return false;                                    // 抛出异常，返回假
    }
    return true;                                         // 返回真
}

// User.php

/**
 * 为用户添加贡献值
 * zhangxishuo
 */
public static function addContribution($username, $contribution) {
    $map['username'] = $username;                        // 定义线索
    $user = self::get($map);                             // 获取用户
    $user->contribution += $contribution;                // 添加贡献值
    try {
        $user->save();                                   // 保存
    } catch (\Exception $e) {
        return false;
    }
    return true;
}
```

# 贡献值管理
