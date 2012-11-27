# 推聊概述 PushTalk Overview
推聊是一个基于极光推送的Android手机聊天系统。支持群聊与点对点聊天。当前包括Android客户端与Java服务器端。3分钟就可以整套系统跑起来。

### 项目官方网站
<http://github.com/good-life/pushtalk>


### 功能与特点
  
##### 功能列表

* 频道里群聊
* 与其他用户点对点聊天
* 聊天支持文字、表情、URL等
* 注册用户（不需要密码）
* 创建频道、加入频道
* 我的最近聊天、我的频道
* 聊天记录缓存（在服务器端）
* 客户端选择聊天服务器、添加聊天服务器

##### 特点

* 简单到极致的 http 聊天服务器：一个 jar 包跑起来
* 轻量级客户端：聊天界面使用 Webview 的方式
* 下行消息推送基于 [JPush](http://jpush.cn/)（一个免费的第三方 Android Push 服务），上行消息采用 http 请求的方式实现


### 聊天服务器

推聊的服务器端使用 Java 语言编写，基于最简单实用的开源框架。

* http 服务器使用 [Jetty](http://www.eclipse.org/jetty/)：轻量级的 Java Servlet 容器。
* 数据库使用 [H2](http://http://www.h2database.com/)： 轻量级的 Java 内存/文件数据库。
* 页面模板使用 [Freemarker](http://freemarker.sourceforge.net/)：轻量级的 Java 页面模板引擎。
* 打包时集成为一个整体 jar 包（包含所有的依赖包）

要运行推聊服务器端，请在 github 文件下载页 <https://github.com/good-life/PushTalk/downloads/> 下载推聊服务器 jar 文件。

使用如下命令运行服务器：

	java -Dfile.encoding=UTF-8 -jar pushtalk-server-with-dependencies.jar
	
`注：实际的文件名后边一般会加上一个版本号。` 

上述命令运行后：

* 一个 http server 启动了，监听端口为 10010
* 浏览器里输入 http://localhost:10010 则可以访问到界面
* 要正确地运行服务器，需要通过客户端访问

推聊服务器默认使用 `10010` 端口。可以在上述命令后指定使用另外的端口。比如以下命令指定使用 `10011` 来启动推聊聊天服务器：

	java -jar pushtalk-server-with-dependencies.jar 10011


### Android 客户端

客户端运行，请在 github 文件下载页 <https://github.com/good-life/PushTalk/downloads/> 下载 Android apk 安装到 Android 手机即可。

你也可以自己编译 `/client-android` 项目之后安装到手机。

客户端默认内置官方聊天服务器地址。也可以直接在客户端添加新的服务器，选择新的聊天服务器。

如果你有服务器资源，可以长期运行，欢迎你架设推聊聊天服务器，请添加到以下的页面。经过测试没有问题，会被选择预置到客户端代码里：

<https://github.com/good-life/PushTalk/wiki/PushTalk-Third-Party-Servers>


### 开发者支持

欢迎广大开发者加入本开源项目，大家一起来不断地丰满、完善推聊。

本人是因为兴趣业余时间来开发推聊，考虑到 IM 占用的时间太多，所以请谅解，不提供任何 IM 的沟通方式。希望大家以如下的方式交流：

* Email:  <pushtalk.org@gmail.com>
* 项目Wiki: <https://github.com/good-life/PushTalk/wiki>
* 项目Bug: <https://github.com/good-life/PushTalk/issues>
* PushTalk频道：`pushtalk`, `报告bug`, `新功能讨论` 等

代码提交方式：请先提交Bug修复，把 diff 以文件方式发邮件给我。合作多次后，我可以把你加入到本项目贡献者里来，你就可以随时自由地提交代码了。

你也可以自由地 fork 本项目，自己去发布新的版本。

推聊使用的开源协议：[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)

