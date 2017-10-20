---
title: maven设置阿里云镜像
date: 2017-10-20 16:57:46
categories: 优化
tags:
- maven
---
`Java`项目中，我们使用`maven`管理依赖，会自动从`maven`中央仓库下载我们需要的`jar`包，但国外的服务器速度总是比不上国内，我们想设置为国内的镜像，以提高下载速度。

<!-- more -->

找到`maven`安装的目录，依次进入目录`/conf/settings.xml`，并用编辑器打开这个文件。

![](/images/2017/10/maven-set-aliyun-mirror/0.png)

在`<mirrors></mirrors>`标签中添加我们需要使用的镜像，这里我们选择阿里云镜像。

```xml
<mirror>
    <id>nexus-aliyun</id>
    <mirrorOf>*</mirrorOf>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
```

保存并关闭，我们的以后需要下载的`jar`包地址，就由原来`maven`国外的中央仓库，转为国内的阿里云镜像，可以提高下载速度。