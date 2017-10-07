---
title: 旅游项目可重用代码
date: 2017-10-02 17:36:07
categories: 总结
tags:
- ThinkPHP
- 代码库
---
旅游项目坎坎坷坷经历了一个月后，终于完工。期间经历了设计原型，设计ER图，建立数据库，完善功能，以及一次重大的需求变更。

经历了不少，也成长了不少。用户心中根本没有一个完整的模型，只是有功能的要求，当我们的系统初步做完时，用户才知道自己要的是一个什么系统，然后会变更一些需求，这是再正常不过的事情了。

总结一下这个项目中可以重用的通用功能代码，以后碰到相似功能不用再劳动一次啦。

<!-- more -->

注：本文中出现的`$param`均为`Request::instance()`。

# 用户功能

## 登陆

登陆，验证输入的账号密码在数据库中是否存在，若存在，则将`id`保存到`session`中。

```php
public function login($param) {

    $message['message'] = '登陆成功';
    $message['status']  = 'success';

    $username = $param->post('username');
    $password = $param->post('password');

    $map['username'] = $username;
    $map['password'] = $password;

    $user = User::get($map);

    if (!is_null($user)) {
        session('userId', $user->id);
    } else {
        $message['message'] = '用户名或密码错误';
        $message['status']  = 'error';
    }

    return $message;
}
```

## 注销

注销就简单了，将`session`中保存的`id`赋值为`null`就可以了。

```php
public function logout() {

    session('userId', null);
    return true;
}
```

## 验证

验证用户登陆，只需要验证`session`中的`userId`存在且不为`null`即可，我们用`isset`验证。

```php
public function isLogin() {

    $userId = session('userId');
    if (isset($userId)) {
        return true;
    }
    return false;
}
```

本项目中，我们还需要对手机预览界面进行处理，如果`url`为手机预览页，则不需要验证登陆。

```php
public function isLogin($param) {

    // 获取url
    $url = $param->path();
    // 切分url字符串为数组
    $urlArray = explode('/', $url);
    
    if ($urlArray[0] === "index" && $urlArray[1] === "article" && $urlArray[2] === "main") {
        return true;
    }

    ......
}
```

# 特殊处理

## 数据库存储数组

上传多张图片，得到多张图片的存储路径，我们将其存储为一个php的数组，但是该数组是不能直接存储进数据库的，所以我们希望能将数组转化为数据库中能够存储的数据格式。

利用`json_encode`，将数组转化为`json`字符串，然后存入数据库；读取时，将字符串取出来，利用`json_decode`，将字符串再转为初始的数组，再进行操作。

## 微信分享

微信分享功能，我们用到了`share.js`这款插件，[share.js项目地址](http://overtrue.me/share.js/)。

# 通用功能

## 查询操作

前台代码：

```html
<form class="form-inline">
    <input class="form-control" type="text" name="materialName" placeholder="素材名称..." value="{:input('get.materialName')}">
    <button type="submit" class="btn btn-default">
        <i class="glyphicon glyphicon-search"></i>
        &nbsp;查询
    </button>
</form>
```

控制器代码：

```php
// 从配置中获取分页数
$pageSize     = config('paginate.var_page');
// 获取通过get方法传来的名称
$materialName = Request::instance()->get('materialName');
// 调用查询逻辑
$materials    = $this->materialService->searchMaterial($materialName, $pageSize);
```

查询逻辑：

```php
public function searchMaterial($materialName, $pageSize) {
    $material = new Material();
    // 模糊查询
    if (!empty($materialName)) {
        $material->where('designation', 'like', '%'. $materialName. '%');
    }
    // 排序分页
    $materials = $material->order('id desc')->paginate($pageSize, false, [
        'query' => [
            'materialName' => $materialName,
        ],
        'var_page' => 'page',
    ]);

    return $materials;
}
```

## 图片操作

图片上传功能

```php
public static function uploadImage($file) {
    $info = $file->validate(['size'=>2048000])->move(PUBLIC_PATH);

    if ($info) {
        return $info->getSaveName();
    } else {
        return $file->getError();
    }
}
```

图片删除功能

```php
public static function deleteImage($imagePath) {
    if ($imagePath !== 'upload/') {
        if (file_exists($imagePath)) {
            unlink($imagePath);
        }
    }
}
```

删除多图功能
```php
public static function deleteManyImages($imagePaths) {
    $imagePaths = json_decode($imagePaths);
    foreach ($imagePaths as $imagePath) {
        $imagePath = PUBLIC_PATH . '/' .$imagePath;
        self::deleteImage($imagePath);
    }
}
```