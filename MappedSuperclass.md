
#@MappedSuperclass的用法 
- `@MappedSuperclass`用在`父类`上面。当这个类`肯定`是`父类`时，加此标注。
如果改成@Entity，则继承后，多个类继承，只会生成`一个表`，而不是多个继承，生成多个表。

**父类：**
```java
@MappedSuperclass
public abstract class BaseEntity {
  private Integer id;// 数据库主键
 private Date creationTime;//创建时间
 private Date modificationTime;//修改时间
```
**子类1：**
```java
public class Test_No1 extends BaseEntity implements Serializable {}
```
**子类2：**
```java
public class Test_NO2 extends BaseEntity implements Serializable {}
```
这样在生成表的时候**只生成**了：`test_no1`、`test_no2`两张表，而且两张表中都含有`id`、`creationTime`、`modificationTime`三个属性
 
但是如果把`@MappedSuperclass`换成`@Entity`那么就会**另外**在生成一张`baseEntity`的表
