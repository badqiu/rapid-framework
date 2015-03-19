# Changelog #
## rapid-generator v4.0 生成器修改 ##
**亮点新特性**
  1. 现模板目录可以是classpath,zip文件.如classpath:template/rapid, c:/template.zip!/some\_subfolder
  1. 生成器的模板路径可以使用freemarker的过滤器,如 ${className^lower\_case},将className转换为小写输出(其实freemarker本身支持${className?lower\_case},但由于疑问号"?"在windows文件路径中是非法字符,所以不能使用?号) 相关过滤器参考[freemarker](http://freemarker.sourceforge.net/docs/ref_builtins_string.html)
  1. 现新的命令行基于maven + groovy脚本编写,更加灵活定制自己的命令行.

**可扩展性**
> 为TableFactory增加TableFactoryListener,可以监听表创建的Table对象,并修改相关属性值等.

**增加参数配置**
  1. generator\_tools\_class : 用于控制存放在生成器中的工具类,以便在模板中使用 StringUtils
  1. tableNameSingularize : 用于控制表名=>className是否自动转换为单数, 如 customers ==> customer. 默认值是 false
  1. data\_source\_jndi\_name : 数据源的jndi名称,如果查找失败,会再次使用jdbc\_url等配置参数
  1. sqlparse\_ignore\_sql\_exception\_error\_codes : 用于SqlFactory解析sql时需要忽略的SqlException的error codes. 使用这个的好处是sql在执行insert update时,可以忽略掉一些外键约束的异常.
**SqlFactory**
> Sql解析能力加强

**新模板**
> 提供一套ibatis的生成模板,可以根据一张表的配置文件,生成IBatisDao,IbatisDaoImpl,Ibatis SqlMap.xml 及相关spring配置文件

**兼容性**

  * **配置不兼容**
> 将jdbc.username等相关配置修改为 jdbc\_username,这样的好处是jdbc\_username可以在模板中直接引用.之前的由于句号"."导致模板难以引用该变量
  * **API不兼容**
> 对GeneratorFacade及一些API的接口进行了比较大的重构. 有不兼容的问题.

> 模板方面仍然完全兼容.


---


## rapid-generator v3.9.2.20100719 生成器修改 ##
  * 增加 gg.queryForList`("select * from user",limit)` 方法
  * 移除表名前缀的配置变量: tableRemovePrefixes
  * 为表配置文件增加 hasOne="ref\_table(ref\_key)" hasMany="ref\_table(ref\_key)",用于在没有外键的情况手工指定,生成one-to-many及many-to-one
  * 增加databaseType=mysql,sqlserver,oracle,db2属性用于dao层的生成
  * generatorBySql()sql解析能力加强