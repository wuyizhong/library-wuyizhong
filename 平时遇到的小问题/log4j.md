 来自: [http://www.blogjava.net/zJun/archive/2006/06/28/55511.html](http://www.blogjava.net/zJun/archive/2006/06/28/55511.html)

Log4J的配置文件(`Configuration File`)就是用来设置记录器的级别、存放器和布局的，它可接key=value格式的设置或xml格式的设置信息。通过配置，可以创建出`Log4J`的运行环境。

#1. 配置文件
Log4J配置文件的基本格式如下：

##配置根Logger
``` 
log4j.rootLogger  =   [ level ]   ,  appenderName1 ,  appenderName2 ,  …
```

##配置日志信息输出目的地Appender
``` 
 log4j.appender.appederName  =  fully.qualified.name.of.appender.class 
　　log4j.appender.appenderName.option1  =  value1 
　　… 
　　log4j.appender.appenderName.optionN  =  valueN 
```
##配置日志信息的格式（布局）
```
log4j.appender.appenderName.layout  =  fully.qualified.name.of.layout.class 
　　log4j.appender.appenderName.layout.option1  =  value1 
　　… 
　　log4j.appender.appenderName.layout.optionN  =  valueN 
```

####其中 [level] 是日志输出级别，共有5级：

**FATAL       0  **
**ERROR      3  **
**WARN       4  **
**INFO         6  **
**DEBUG      7 **


####Appender 为日志输出目的地，Log4j提供的appender有以下几种：
```
org.apache.log4j.ConsoleAppender（控制台），
org.apache.log4j.FileAppender（文件），
org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），
org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件），
org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
```

####Layout：日志输出格式，Log4j提供的layout有以下几种：
```
org.apache.log4j.HTMLLayout（以HTML表格形式布局），
org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
```

####打印参数: Log4J采用类似C语言中的printf函数的打印格式格式化日志信息，如下:

> 　 %m   输出代码中指定的消息
　　%p   输出优先级，即`DEBUG`，`INFO`，`WARN`，`ERROR`，`FATAL` 
　　%r   输出自应用启动到输出该log信息耗费的**毫秒数** 
　　%c   输出所属的类目，通常就是所在类的全名 
　　%t   输出产生该日志事件的线程名 
　　%n   输出一个回车换行符，Windows平台为`“/r/n”`，Unix平台为`“/n”` 
　　%d   输出日志时间点的日期或时间，默认格式为`ISO8601`，也可以在其后指定格式，比如：`%d{yyy MMM dd HH:mm:ss , SSS}`，输出类似：2002年10月18日  22 ： 10 ： 28 ， 921  
　　%l   输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。举例：```Testlog4.main(TestLog4.java: 10 ) ```

#2. 在代码中初始化Logger: 
1）在程序中调用```BasicConfigurator.configure()```方法：给根记录器增加一个`ConsoleAppender`，输出格式通过`PatternLayout`设为`"%-4r [%t] %-5p %c %x - %m%n"`，还有根记录器的默认级别是`Level.DEBUG`. 
2）配置放在文件里，通过命令行参数传递文件名字，通过```PropertyConfigurator.configure(args[x])```解析并配置；
3）配置放在文件里，通过环境变量传递文件名等信息，利用log4j默认的初始化过程解析并配置；
4）配置放在文件里，通过应用服务器配置传递文件名等信息，利用一个特殊的servlet来完成配置。

#3. 为不同的 Appender 设置日志输出级别：
当调试系统时，我们往往注意的只是异常级别的日志输出，但是通常所有级别的输出都是放在一个文件里的，如果日志输出的级别是BUG！？那就慢慢去找吧。
这时我们也许会想要是能把异常信息单独输出到一个文件里该多好啊。当然可以，Log4j已经提供了这样的功能，我们只需要在配置中修改`Appender`的`Threshold` 就能实现,比如下面的例子：

**[配置文件]**
```
 ### set log levels ###
log4j.rootLogger = debug ,  stdout ,  D ,  E

### 输出到控制台 ###
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern =  %d{ABSOLUTE} %5p %c{ 1 }:%L - %m%n

### 输出到日志文件 ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/log.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = DEBUG ## 输出DEBUG级别以上的日志
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

### 保存异常信息到单独文件 ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/error.log ## 异常日志文件名
log4j.appender.D.Append = true
log4j.appender.D.Threshold = ERROR ## 只输出ERROR级别以上的日志!!!
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```

**[代码中使用] **
```java
 public   class  TestLog4j  {
     public   static   void  main(String[] args)  {
        PropertyConfigurator.configure( " D:/Code/conf/log4j.properties " );
        Logger logger  =  Logger.getLogger(TestLog4j. class );
        logger.debug( " debug " );
        logger.error( " error " );
    } 
}
```
