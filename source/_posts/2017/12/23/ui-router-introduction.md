---
title: UI-Router入门
date: 2017-12-23 18:51:54
categories: 学习
tags:
- AngularJS
---
因为我们现在开发的应用往往需要嵌入式视图，而`AngularJS`中的`ngRoute`只允许一个视图出现在页面上，所以我们使用`UI-Router`来满足我们的要求。

使用`UI-Router`中的`ui-view`，我们可以将各个视图分离开来，最后将需要的视图进行嵌套，以满足用户的需求。

<!-- more -->

# 安装

我们使用`bower`来安装我们的`ui-router`模块。打开终端，进入前台项目，输入以下命令：

```bash
bower install angular-ui-router --save
```

`bower install angular-ui-router`是`bower`为我们安装`angular-ui-router`的命令，而`--save`是为我们在`bower.json`中添加一条对该模块的依赖。

![](/images/2017/12/23/ui-router-introduction/0.png)

# 路由

安装完了，我们来看看`UI-Router`的`Router`是怎么用的吧。

```javascript
angular
    .module('webAppApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ui.router'    // 声明该模块依赖于ui-router
    ])
    .config(function($stateProvider, $urlRouterProvider) {

        /**
         * 声明路由
         * $stateProvider.state({})
         * state方法用于声明一个路由
         * 传入一个含有配置路由参数的对象
         */
        $stateProvider
            .state({
                name: 'main',                       // 声明名称
                url: '/main',                       // 声明url
                controller: 'MainCtrl',             // 声明控制器名称
                templateUrl: 'views/main.html'      // 声明模板文件地址
            })
            .state({
                name: 'main.add',                   // 声明名称, main.add表示该路由继承于main
                url: '/add',                        // 声明url, 因为继承自main, 所以完整的url为/main/add
                controller: 'MainAddCtrl',          // 声明控制器名称
                templateUrl: 'views/main/add.html'  // 声明模板文件地址
            })
            .state({
                name: 'main.edit',
                url: '/edit/:id',
                controller: 'MainEditCtrl',
                templateUrl: 'views/main/edit.html'
            });

        $urlRouterProvider
            .otherwise('/main');
    });
```
