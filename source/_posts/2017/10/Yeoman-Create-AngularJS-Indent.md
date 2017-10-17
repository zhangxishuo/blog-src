---
title: Yeoman创建AngularJS项目代码缩进问题
date: 2017-10-17 21:54:27
categories: 问题
tags:
- Indent
---
用`yeoman`创建`AngularJS`项目，打开项目之后，我们会发现代码的默认缩进是两个字符，这让已经习惯四个空格作为缩进的我们非常难受，而我们的编辑器已经修改为四个空格缩进，但是这样设置并不生效。

<!-- more -->

打开终端，进入项目文件夹，查看隐藏文件，`ls -a`。

    zhangxishuo@zhangxishuo-Inspiron-7559:~/桌面/FullStack/webapp$ ls -a                             
    .    bower_components  .editorconfig   Gruntfile.js  .jshintrc     package-lock.json  .tmp       
    ..   bower.json        .gitattributes  .idea         node_modules  README.md          .yo-rc.json
    app  .bowerrc          .gitignore      .jscsrc       package.json  test                          

根据名称大致可以理解这里有一个名为`.editorconfig`的隐藏文件。从名称可以大致推断出这个文件可能与我们要修改的代码缩进有关。

编辑这个文件，得到如下内容。

    # EditorConfig helps developers define and maintain consistent
    # coding styles between different editors and IDEs
    # editorconfig.org

    root = true


    [*]

    # Change these settings to your own preference
    indent_style = space
    indent_size = 2

    # We recommend you to keep these unchanged
    end_of_line = lf
    charset = utf-8
    trim_trailing_whitespace = true
    insert_final_newline = true

    [*.md]
    trim_trailing_whitespace = false

`.editorconfig`是一项编辑时的配置文件，该文件中的配置默认会覆盖编辑器中的配置。

我们看到配置中有一项`indent_size = 2`，将其修改为4，即可实现四个空格缩进。