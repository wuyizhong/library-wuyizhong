# 1、主从复制
(来自：http://blog.csdn.net/hguisu/article/details/7325124)
## 1.1、创建复制帐号
+ 1、在`Master`的数据库中建立一个备份帐户：每个`slave`使用标准的`MySQL`用户名和密码连接`master`。进行复制操作的用户会授予`REPLICATION SLAVE`权限。用户名的密码都会存储在文本文件`master.info`中

命令如下：
```
mysql > GRANT REPLICATION SLAVE,RELOAD,SUPER ON *.*
TO repl@’192.168.1.100’
IDENTIFIED BY ‘123456’;
```
建立一个帐户`repl`，并且只能允许从`192.168.1.100`这个地址上来登陆，密码是`123456`。
(如果因为`mysql`版本新旧密码算法不同，可以设置：
```
set password for 'backup'@'10.100.0.200'=old_password('1234')）
```

## 1.2、拷贝数据
（假如是你完全新安装`mysql`主从服务器，这个一步就不需要。因为新安装的`master`和`slave`有相同的数据）

关停`Master`服务器，将`Master`中的数据拷贝到B服务器中，使得`Master`和`slave`中的数据同步，并且确保在全部设置操作结束前，**禁止在Master和slave服务器中进行写操作，使得两数据库中的数据一定要相同！**

## 1.3、配置`master`
接下来对`master`进行配置，包括打开二进制日志，指定唯一的`servr ID`。例如，在配置文件加入如下值：
```
#slave会基于此log-bin来做replication
log-bin=mysql-bin
log-bin-index=mysql-bin.index
#master的标示
server-id=1
```
+ `server-id`：为主服务器A的ID值
+ `log-bin`：二进制变更日值

**重启** `master`，运行`SHOW MASTER STATUS`，记录File的内容:`mysql-bin.000001`

## 1.4、配置`slave`
`slave`的配置与`master`类似，你同样需要**重启**`slave`的`MySQL`。如下：
```
log_bin           = mysql-bin
server_id         = 2
relay_log         = mysql-relay-bin
log_slave_updates = 1
read_only         = 1
```
`server_id`是必须的，而且唯一。`slave`没有必要开启二进制日志，但是在一些情况下，必须设置，例如，如果`slave`为其它`slave`的`master`，必须设置bin_log。在这里，我们开启了二进制日志，而且显示的命名(默认名称为hostname，但是，如果hostname改变则会出现问题)。

`relay_log`配置中继日志;

`log_slave_updates`表示`slave`将复制事件写进自己的二进制日志(后面会看到它的用处)。

有些人开启了`slave`的二进制日志，却没有设置`log_slave_updates`，然后查看`slave`的数据是否改变，这是一种错误的配置。所以，尽量使用`read_only`，它防止改变数据(除了特殊的线程)。但是，`read_only`并是很实用，特别是那些需要在slave上创建表的应用。
## 1.5、启动slave
接下来就是让`slave`连接`master`，并开始重做`master`二进制日志中的事件。你不应该用配置文件进行该操作，而应该使用`CHANGE MASTER TO`语句，该语句可以完全取代对配置文件的修改，而且它可以为`slave`指定不同的`master`，而不需要停止服务器。如下：
```
mysql> CHANGE MASTER TO MASTER_HOST='192.168.1.128',
    -> MASTER_USER='repl',
    -> MASTER_PASSWORD='123456',
    -> MASTER_LOG_FILE='mysql-bin.000001',
    -> MASTER_LOG_POS=0;
```
`MASTER_LOG_POS`的值为`0`，因为它是日志的开始位置。

你可以用`SHOW SLAVE STATUS`语句查看slave的设置是否正确：
```
mysql> SHOW SLAVE STATUS\G
```
在这里主要是看:

**                   Slave_IO_Running=Yes**
**                   Slave_SQL_Running=Yes**

`slave`的I/O和SQL线程都已经开始运行，而且`Seconds_Behind_Master`不再是`NULL`。日志的位置增加了，意味着一些事件被获取并执行了。如果你在master上进行修改，你可以在slave上看到各种日志文件的位置的变化，同样，你也可以看到数据库中数据的变化。

你可查看`master`和`slave`上线程的状态。在master上，你可以看到slave的I/O线程创建的连接：
在`master`上输入
```
show processlist\G;
```
在`slave`上输入
```
show processlist\G;
```

# 2慢查询
（来自：http://blog.csdn.net/ljasdf123/article/details/9713523）
MYSQL慢查询配置

+ 2.1 慢查询有什么用?

它能记录下所有执行超过`long_query_time`时间的`SQL`语句, 帮你找到执行慢的`SQL`, 方便我们对这些`SQL`进行优化.

+ 2.2 如何开启慢查询?

首先我们先查看`MYSQL`服务器的慢查询状态是否开启.执行如下命令:
```
show variables like '%quer%';
```

我们可以看到当前`log_slow_queries`状态为`OFF`, 说明当前并没有开启慢查询.

开启慢查询非常简单, 操作如下:

在`[mysqld]`中添加如下信息：

```properties
[mysqld]
log-slow-queries="C:/Program Files/MySQL/MySQL Server 5.5/log/mysql-slow.log"
long_query_time = 4

log-queries-not-using-indexes
```
`log-slow-queries`: 代表`MYSQL`慢查询的日志存储目录, 此目录文件一定要有写权限；

`Windows`下需要写**绝对**路径，如：`log-slow-queries="C:/Program Files/MySQL/MySQL Server 5.5/log/mysql-slow.log"`

`long_query_time`: 最长执行时间. (如图, `MSYQL`将记录下所有执行时间超过2条的SQL语句, 此处为测试时间, 时间不应太小最好在5-10秒之内, 当然可以根据自己的标准而定);

`log-queries-not-using-indexes`：没有使用到索引的查询也将被记录在日志中
配置好以后**重启**`MYSQL`服务
