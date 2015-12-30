# 1. APACHE支持.htaccess以及 No input file specified解决方案

你的Apache安装文件夹conf里找到httpd.conf文件

索LoadModule rewrite_module modules/mod_rewrite.so 如果前面有注释符号#，请去掉。
搜索Options FollowSymLinks，然后将它下面的AllowOverride None 修改为AllowOverride All；

## 【1】

没想到遇见了 No input file specified   因为项目用了URL route ，估摸着可能是rewrite的问题。

记录一下解决方案。

* 1.检查doc_root 是否设置此值
* 2.检查.hta文件 , 很多框架都是index.php当入口文件。
默认的
```php
RewriteRule ^(.*)$ index.php/$1 [QSA,PT,L]
```
规则在apache fastcgi模式下会导致No input file specified.

修改成
```php
RewriteRule ^(.*)$ index.php [L,E=PATH_INFO:$1]
```
> 使用这条成功了

就OK，地址正常重写。

## 【2】

我们都知道，使用伪静态相对来说，对搜索引擎比较友好，而我在`Dreamhost`的空间上启用`REWRITE`的伪静态功能的时候，首页可以访问，而访问内页的时候，就提示：“No input file specified.”。
百度搜索了一下，发现还有其它空间商也有此问题，原因在于空间所使用的`PHP`是`fast_cgi`模式，而在某些情况下，不能正确识别`path_info`所造成的错误，就是`Wordpress`也有一样的问题，还好找到了解决方案！
我们首先来看一下`Wordpress`及`Typecho`等程序默认的`.htaccess`里面的规则：
```php
RewriteEngine On
RewriteBase /
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^(.*)$ /index.php/$1 [L]
```

而提示是说：“No input file specified.”，也就是说没有得到有效的文件路径。在Google中找到了解决方案，就是修改一下伪静态规则，如下：
```php
RewriteEngine On
RewriteBase /
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^(.*)$ /index.php?/$1 [L]
```

在正则结果“$1”前面多加了一个“?”号，问题也就随之解决了。
