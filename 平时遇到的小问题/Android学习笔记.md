#Android学习笔记
###1 安装
* Android Studio一直提示安装SDK连接失败
	修改bin/idea.properties 文件，在文件最后追加
```properties 
disable.android.first.run=true
```
**记得加回车**，不然不好使。

###2 签名Android应用程序
* 作用：
	+ 确定发布者的身份
	+ 确保应用的完整性

	**如果要正式发布一个Android应用，必须使用合适的数字证书来给应用程序签名，不能使用Android Studio或Ant工具自动生成的调试证书来发布。**

###3 界面编程
	+ `match_parent`: 该属性值与`fill_parent`完全相同，从2.2开始就推荐使用。
	+ `warp_content`: 指定子组件的大小恰好能包裹他的内容即可。
