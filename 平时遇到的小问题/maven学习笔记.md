## 1 maven 项目无法发布到tomcat下(404错误)
项目web部署组件没有配置好
打开项目属性，找到其中的`web deployment assembly`这个标签
然后你需要在里面把你的web根路径和jar包路径添加进去，比如`/src/main/webapp``Maven lib`等
这样再部署到TOMCAT上应该就可以了

*来自http://bbs.csdn.net/topics/390900092*
