#redis学习笔记
##一、Redis安装
* 1、buntu上安装Redis
```SHELL
$sudo apt-get update
$sudo apt-get install redis-server
```
* 2启动Redis
```SHELL
$redis-server
```

* 3、检查Redis是否在工作
```SHELL
$redis-cli
```
输入`PING`命令：
```SHELL
redis 127.0.0.1:6379> ping
PONG
```
这说明已经`成功`安装了Redis。
##二、Redis配置
* 1、Redis的CONFIG命令语法：
    查看配置项信息
```shell
redis 127.0.0.1:6379> CONFIG GET CONFIG_SETTING_NAME
```
可以使用`*`来代替配置项名称

    编辑配置项信息
```shell
redis 127.0.0.1:6379> CONFIG SET CONFIG_SETTING_NAME NEW_CONFIG_VALUE
```
##三、Redis的数据类型
包括：
* 1、Stirngs-字符串
Redis的字符串是字节序列，Redis的字符串是二进制安全的，他们有一个已知的长度，没有任何特殊字符决定终止，可以存储任何东西，最大长度可达512M。
```shell
set username "name"
get username
```
* 2、Hash-哈希值
Redis的哈希值是键值对的集合。Redis的哈希值是字符串字段和字符串值之间的映射，用来表示对象。
```shell
HMSET test:1 username test1 password pwd point 200
HGETALL test:1
```
这里`HMSET`、`HGETALL`命令`test:1`是键。
每个哈希可存储多达232 - 1个`字段-值`对(超过4十亿)。
* 3、Lists - 列表
Redis的列表是简单的字符串列表，排序插入顺序。可以添加元素到Redis列表的头部或尾部。
```shell
lpush goodslist goods1
lrange goodslist 0 10
```
列表的最大长度为232-1元素(4294967295，每个列表中的元素超过4十亿)。
* 4、Sets - 集合
Redis集合是字符串的无序集合。在Redis中可以添加，删除和测试文件是否存在在O(1)的时间复杂度的成员。
```shell
sadd goodslist goods1
smembers goodslist 0 10
```
*因为set的唯一性，不能添加重复的属性。*
成员中集最大数量为232-1(4294967295，集合成员超过4十亿)。
* 5、集合排序
Redis的集合排序的每个成员有个排序值，以便Redis采用有序的set命令，排序为由小到大。成员都是独一无二的，但是排序值可能会重复。
```shell
zadd goodslist goods1
ZRANGEBYSCORE goodslist 0 10
```

##四、Redis命令
* 打开终端：
```shell
$redis-cli
redis 127.0.0.1:6379>
redis 127.0.0.1:6379> PING
PONG
```
* 在远程服务器上运行命令：
```shell
$ redis-cli -h host -p port -a "password"
```
##四、Redis操作
* 设置密码
```shell
redis> CONFIG SET requirepass secret_password
```
* 清空密码
```shell
redis> CONFIG SET requirepass ""
```

##五、Redis数据结构与对象
* 1、动态字符串
`只会`作为字符串字面量，用在有些无需对字符串值进行修改的地方，比如打印日志：
```c
redisLog(REDIS_WARNING, "Redis is ...");
```
当Redis需要的不仅仅是一个字符串字面量，而是一个可以被修改的字符串值时，Redis就会使用SDS来表示字符串值。
除了用来保存数据库中的字符串值之外，SDS还被用作缓冲区：AOF模块中的AOF缓冲区，以及客户端状态中的输入缓冲区。
