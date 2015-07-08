链接地址： http://blog.csdn.net/fengxinze/article/details/7186765

原文：-- 已经404


接到任务，需要编程求两个十万级记录列表的交集。分析后，有两个解决思路：一、在数据库中建立临时表，导入数据后，用SQL语句才交集，再导出数据；二、直接内存运算求解。考虑到数据记录多，数据库运算压力大，最后决定选择第二个方法。

 

涉及到数据查找比对，首先考虑到使用HashSet。HashSet最大的好处就是实现查找时间复杂度为O（1）。使用HashSet需要解决一个重要问题：冲突问题。对比研究了网上一些字符串哈希函数，发现几乎所有的流行的HashMap都采用了DJB Hash Function，俗称“Times33”算法。Times33的算法很简单，就是不断的乘33，见下面算法原型。

```java
hash(i) = hash(i-1) * 33 + str[i]  
```

Time33在效率和随机性两方面上俱佳。对于一个Hash函数，评价其优劣的标准应为随机性，即对任意一组标本，进入Hash表每一个单元（cell）之概率的平均程度，因为这个概率越平均，数据在表中的分布就越平均，表的空间利用率就越高。

 

```java
int Time33(String str) {  
    int len = str.length();  
    int hash = 0;  
    for (int i = 0; i < len; i++)  
        // (hash << 5) + hash 相当于 hash * 33  
        hash = (hash << 5) + hash + (int) str.charAt(i);  
    return hash;  
}  
``` 

Java的HashSet判断冲突，主要依靠对象的HashCode和equals方法。因此对象必须重写hashCode和equals两个方法。

以下测试构造300,0000个Item对象，放入到HashSet中，测试Time33算法。

 

```java
/** 
 * Item类 
 * 重写hashCode和equals方法 
 */  
class Item {  
    public Item(String[] strs) {  
        columns = strs;  
    }  
  
    public boolean equals(Object otherObject) {  
        if (this == otherObject)  
            return true;  
        if (otherObject == null)  
            return false;  
        if (getClass() != otherObject.getClass())  
            return false;  
        if (!(otherObject instanceof Item))  
            return false;  
        Item other = (Item) otherObject;  
        if (this.columns.length != other.columns.length)  
            return false;  
        for (int i = 0; i < this.columns.length; i++) {  
            if (this.columns[i] != null && !this.columns[i].equals(other.columns[i]))  
                return false;  
        }  
        return true;  
    }  
  
    public int hashCode() {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < this.columns.length; i++) {  
            sb.append(this.columns[i]);  
        }  
        return this.Time33(sb.toString());  
    }  
  
    private int Time33(String str) {  
        int len = str.length();  
        int hash = 0;  
        for (int i = 0; i < len; i++)  
            hash = (hash << 5) + hash + (int) str.charAt(i);  
        return hash;  
    }  
  
    private String[] columns;  
}  
```

```java
HashSet<Item> hashSet = new HashSet<Item>();  
long start;  
long end;  
String[] strs = { "Alvin", "Chan", "HashSet", "WOW" };  
String[] tmp;  
int count = 0;  
      
start = System.currentTimeMillis();  
  
for (int i = 0; i < 3000000; i++) {  
    tmp = new String[4];  
    for (int j = 0; j < 4; j++) {  
        // 用strs中随机抽取字符串，并加入随机数，生成tmp字符串数组  
        tmp[j] = strs[(int) (Math.random() * 3)] + (int) (Math.random() * i);  
    }  
    // 加入无法插入到hashSet中，计数器加1  
    if(!hashSet.add(new Item(tmp))) {  
        count++;  
    }  
}  
  
end = System.currentTimeMillis();  
  
System.out.println("插入300,0000条记录");  
System.out.println("所需时间：" + (end - start) + " ms");  
System.out.println("插入个数：" + hashSet.size());  
System.out.println("失败次数：" + count);  
``` 

测试运行了5次，结果如下：（数据量较大，建议加大JVM的内存再运行测试，否则会内存溢出）

```
第1次  
插入300,0000条记录  
所需时间：34203 ms  
插入个数：3000000  
失败次数：0  
  
第2次  
插入300,0000条记录  
所需时间：33063 ms  
插入个数：3000000  
失败次数：0  
  
第3次  
插入300,0000条记录  
所需时间：33016 ms  
插入个数：3000000  
失败次数：0  
  
第4次  
插入300,0000条记录  
所需时间：33062 ms  
插入个数：3000000  
失败次数：0  
  
第5次  
插入300,0000条记录  
所需时间：33140 ms  
插入个数：3000000  
失败次数：0  
```

从测试结果来看，面对百万级条记录，使用了Time33作为哈希函数的HashSet能较好地解决冲突问题。
