#@MappedSuperclass的用法 
##@MappedSuperclass 用在父类上面。当这个类肯定是父类时，加此标注。
如果改成@Entity，则继承后，多个类继承，只会生成一个表，而不是多个继承，生成多个表
