

# 不要提问的问题 #

不要问如hibernate的sequence id generator怎么配置的问题,此类问题你查阅hibernate文档即可.
包括struts,struts2,springmvc,ibatis这些框架你应该本身了解它的运作原理,rapid只是一个脚手架,帮你将各个框架组合好搭建在一起,有rapid扩展的部分已经说明,如具体碰到框架使用问题请查询各个框架本身的文档.

# 一般问题 #
## 默认为何没有使用国际化? ##
  1. 一般的系统如果没有国际化需求，首先应该考虑的就是不要支持国际化。而国内一般的系统实际没有国际化需求，以避免在此浪费精力。所以现在系统默认没有使用国际化.
  1. 使用'<%=Blog.ALIAS\_TITLE%>'可以在编译时期就发现错误.


## service层及dao层为何不使用接口? ##
一般项目，service层及dao一般只有一个实现，为此多些接口类实在是累赘。所以生成的代码不是基于接口的。
当然接口的好处是可以一目也然的查看接口拥有的方法列表,所以
可以根据自己需要对生成器模板进行修改。 这里还有我写的一篇文章: [接口滥用问题](http://badqiu.javaeye.com/blog/781626)

## struts2为何报`setParameters : Unexpected Exception caught setting 'property' on class com.company.action.UserAction` ##
在struts.xml将开发模式改为false即可
```
<constant name="struts.devMode" value="false" />
```

## 为何要整三个spring文件及多处context:component-scan分散配置，而不是一个 ##
  * 可以分类，太多配置集中在一起不好.
  * 单元测试时在不同的层测试可以避免加载全部的类进来。比如在测试dao层时，spring就没有必要加载service层的manager在applicationContext

## 为何我的Eclipse的Problems Tab报很多Errors ##
这个是由于MyEclipse验证导致,因为现在的生成器模板都是以.java,.xml结尾,但它们不是合法的java,xml文件,所以验证时会报错.

**解决办法:**可以设置 "项目属性=>MyEclipse=>Validation=>Excluded Resources"选择template目录并保存

# 代码生成器 #
## 生成器生成半途报错 ##
一般是表没有主键导致，可以查看报错的stack trace, freemarker的模板报错原因是比较详细的.

## 为何没有生成dao,jsp等页面? ##
现在整个项目为插件体系，需要安装相关插件，才会生成文件。
具体请查看项目的doc目录下面的文档.
具体你也可以查看template目录有没有jsp等内容，因为生成器会加载该目录的模板然后生成代码的。

## 代码生成器的hibernate模板可不可以生成one-to-many,many-to-one? ##
可以，只要两张表之间有外键关系就会自动生成.

## 代码生成器的hibernate模板可不可以生成many-to-many? ##
现可以生成many-to-one,one-to-many,但不能生成many-to-many,这个需要自己手写。

## 是否支持从`PowerDesigner`生成代码? ##
现暂不支持，rapid中关心最核心的部分，生成代码。而`PowerDesigner`的表也可以在数据库中建表后再生成。

## Exception in thread "main" java.lang.RuntimeException: create table object error,tableName:BIN$/0cekC/HTwaKQCpYhs6EoQ==$0 ##
Caused by: java.sql.SQLException: ORA-01424: 转义符之后字符缺失或非法.

问题原因：oracle回收站问题

解决办法:
  1. 可以使用命令: purge recyclebin;清空回收站。
  1. 生成文件时指定具体的表名.

