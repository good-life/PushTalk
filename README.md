# 推聊简介 PushTalk Overview
推聊是一个基于[极光推送][JPush]的简易聊天系统。支持群聊与点对点聊天。

### 项目官方网站
http://github.com/good-life/pushtalk  

### 功能与特点
  
##### 功能列表

* 频道里群聊
* 与其他用户点对点聊天
* 聊天支持文字、表情、URL等
* 创建频道、加入频道
* 注册用户（不需要密码）
* 我的频道
* 我的最近聊天
* 聊天记录缓存
* 客户端选择聊天服务器、添加聊天服务器

##### 特点

* 简单到极致的 http 聊天服务器：一个 jar 包跑起来
* 轻量级客户端：聊天界面使用 Webview 的方式
* 下行消息推送基于 [JPush][]，上行消息采用 http 实现

### 聊天服务器

推聊的服务器端使用 Java 语言编写，基于最简单实用的开源框架。

* http 服务器使用 [Jetty][]
* 数据库使用 [H2][]
* 页面模板使用 [Freemarker][]
* 打包时集成为一个整体 jar 包（包含所有的依赖包）

要运行推聊服务器端，请在取得的源代码里找到这个文件 ``/dist/pushtalk-server-with-dependencies.jar``

使用如下命令运行服务器：

	java -jar pushtalk-server-with-dependencies.jar
	
上述命令运行后：

* 一个 http server 启动了，监听端口为 10010
* 浏览器里输入 http://localhost:10010 则可以访问到界面
* 要正确地运行服务器，需要通过客户端访问

推聊服务器默认使用 10010 端口。可以在上述命令后指定使用另外的端口。比如以下命令指定使用 10011 来启动推聊聊天服务器：

	java -jar pushtalk-server-with-dependencies.jar 10011


### Android 客户端

客户端运行，只需在源代码里找到 `/dist/PushTalk-x.x.apk` 安装到 Android 手机即可。你也可以自己编译 `/client-android` 项目之后安装到手机。

客户端默认内置官方聊天服务器地址。也可以直接在客户端添加新的服务器，选择新的聊天服务器。

```
如果你有服务器资源，可以长期运行，欢迎你架设推聊聊天服务器，提供给我们，预置到客户端代码里。
```


[JPush]: http://jpush.cn *极光推送官方网站*
[Jetty]: http://eclipse.org/projects/jetty *Jetty官方网站*
[H2]: http://h2-database.org *H2 轻量级 Java 数据库官方网站*
[Freemarker]: http://freemarker.org *Freemarker 模板语言官方网站*

