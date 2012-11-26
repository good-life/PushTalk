# 推聊服务器运行与打包

### 直接运行代码
![image](http://)

如上图。在代码树里，找到 JettyServer 这个源代码文件。点击右键，选择：运行 -> Java应用。
要特别注意，不要选择在服务器上运行。

### 打包集成 jar
![image](http://)

如上图，在 Eclipse 里，选择 pushtalk-server 项目，点击右键，选择：导出。

在弹出的对话框里，选择导出类型为：可执行的 jar 包。


### 运行集成 jar 
在取得的源代码里找到这个文件 ``/dist/pushtalk-server-with-dependencies.jar``

使用如下命令运行服务器：

	java -jar pushtalk-server-with-dependencies.jar

