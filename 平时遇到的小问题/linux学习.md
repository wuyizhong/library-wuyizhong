#linux 学习
## 基本语法
获取root权限 ：  sudo su
删除 ： rmdir
查看IP地址的命令： ifconfig
查看版本信息： cat proc/version
显示系统名、节点名称、操作系统的发行版号、操作系统版本、运行系统的机器 ID 号： uname -a

关机： shutdown -h now

翻页：shift+PageUp/PageDown


开放端口：netstat -apn|grep 6379
查看redis：ps aux|grep redis

windows测试远程主机上的6379端口是否开启
1、安装telnet。我的win7下就没有telnet，在cmd下输入telnet提示没有该命令。于是我们进入控制面板---->程序---->打开或关闭windows功能。
2、下面开始测试某个端口是否开启，在cmd下输入:
telnet 192.168.3.42 2121

## 2.安装jre
### 第一步：下载jdk/jre
```
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u71-b14/jdk-7u71-linux-x64.rpm 
```
(注：如果下载不下来，建议使用迅雷下载，然后拷贝到Linux系统上。)
 
### 第二步：解压安装
```
sudo tar zxvf jdk-7u71-linux-x64.rpm  -C /usr/lib/jvm  
cd /usr/lib/jvm  
sudo mv jdk1.7.0/ java-7-sun  
```
> 如果解压报错，一般是下载的文件有问题

### 第三步：修改环境变量
```
vim ~/.bashrc  
```
添加：
```
export JAVA_HOME=/usr/lib/jvm/java-7-sun  
export JRE_HOME=${JAVA_HOME}/jre  
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
export PATH=${JAVA_HOME}/bin:$PATH  
```
保存退出，输入以下命令使之立即生效。
```
source ~/.bashrc  
```
> 遇到问题：The command could not be located because '/usr/bin' is not included
为了把adb命令设置到环境变量中，我在~/.bashrc文件中加入命令，导致系统PATH的路径错误，所以系统工具命令都找不到。
可怎么把配置文件改过来呢，折腾了半天，发现很简单。直接用系统工具的全路径编辑配置文件。我用的是
```
$vi $HOME/.bash_profile 
```
修改
`export PATH=` 最后加`:$PATH`
重启机器

### 第四步：配置默认JDK版本
由于Ubuntu中可能会有默认的JDK，如openjdk，所以，为了将我们安装的JDK设置为默认JDK版本，还要进行如下工作。
执行代码:
```
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-7-sun/bin/java 300  
sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-7-sun/bin/javac 300  
```
执行代码：
```
sudo update-alternatives --config java  
```
系统会列出各种JDK版本，如下所示：
```
www.linuxidc.com@linux:~$ sudo update-alternatives --config java  
```
有 3 个候选项可用于替换 java (提供 /usr/bin/java)。  
  选择       路径                                    优先级  状态  
  | --- | ---- | --- | ---- |
  
 *0            /usr/lib/jvm/java-6-openjdk/jre/bin/java   1061      自动模式  
  1            /usr/lib/jvm/java-6-openjdk/jre/bin/java   1061      手动模式  
  2            /usr/lib/jvm/java-6-sun/jre/bin/java       63        手动模式  
  3            /usr/lib/jvm/java-7-sun/bin/java           300       手动模式  
  
  
要维持当前值[*]请按回车键，或者键入选择的编号：3  
update-alternatives: 使用 /usr/lib/jvm/java-7-sun/bin/java 来提供 /usr/bin/java (java)，于 手动模式 中。  
### 第五步：测试
```
www.linuxidc.com@linux:~$ java -version  
```
> 如果报错`bash: /usr/bin/java: No such file or directory`，一般是版本装错了，64装了32的版本
