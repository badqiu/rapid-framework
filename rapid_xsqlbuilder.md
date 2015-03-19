

# 简介 #
xsqlbuider是类似iBatis的动态sql构造工具

项目网站http://code.google.com/p/rapid-xsqlbuilder/

# 特性 #
  * 动态构造sql条件语句,提供sql拼接与使用占位符两种方式
  * 对SQL注入攻击的防范

# 创建xsqlbuilder的原因 #
## 问题 ##
手工构造SQL语句的情况
```
	String sql = "select * from user where 1=1 ";
	String user_id = (String) filters.get("user_id");
	if (user_id != null && user_id.length() > 0) {
		sql = sql + " and user_id = " + user_id;
	}
	String age = (String) filters.get("age");
	if (age != null && age.length() > 0) {
		sql = sql + " and age > " + age;
	}
```
过多的if判断导致sql语句不清晰,我们再来看下rapid-xsqlbuilder的做法

## rapid-xsqlbuilder构造SQL例子 ##
```
	// 清晰的sql语句,/~ ~/为一个语法块
	 String sql= "select * from user where 1=1 " 
	         + "/~ and username = {username} ~/"   
	         + "/~ and password = {password} ~/";   
	 
	 // filters为参数
	 Map filters = new HashMap();   
	 filters.put("username", "badqiu"); 
	 filters.put("sex", "F");  
	 
	 XsqlFilterResult result = new XsqlBuilder().generateHql(sql,filters);
	 
	 assertTrue(result.getAcceptedFilters().containsKey("username"));
	 assertFalse(result.getAcceptedFilters().containsKey("sex"));
	 assertEquals("select * from user where 1=1  and username = :username ", result.getXsql());
```

XsqlFilterResult为处理完返回的东西,包含两个属性xsql,acceptedFilters
被过滤掉的东西:
> SQL过滤: /~ and password = {password} ~/
> 这一段由于在filters中password不存在而没有被构造出来
> filters过滤: sex
> filters中由于没有类似/~ sex={sex} ~/ 这一段,所以在过滤完的filters中不存在
最终构造生成的结果
HQL: XsqlFilterResult.xsql属性
select **from user where 1=1 and username=:username**

构造后返回的Map filters: XsqlFilterResult.acceptedFilters 属性
username=badqiu

# 语法 #
## 语法 ##
```
/~ {key} ~/
/~ [key] ~/
/~ {key_1} [key_2] ... {key_3} ~/
```

## 示例 ##
```
/~ username = {username} ~/
/~ password like '%[password]%' ~/
/~ birthDate > {startBirthDate} and birthDate < [endBirthDate] ~/
```


## `中括号[]与大括号{}的区别(用于支持参数绑定)` ##

### 中括号会直接替换为其值,用于拼接SQL ###
```
在XsqlFilterResult.getAcceptedFilters()中不会存在该key的值
如 `/~ username like '%[username]%' ~/`,如果filters中username=badqiu
则会生成: username like '%badqiu%'
```

### 大拓号只是起到标记作用,用于占位符 ###
```
原始方法是XsqlBuilder.applyFilters(sql,filters);
如`/~ and username = {username} ~/`,过滤完还是为 and username = {username}
但在这时我们使用将{username}替换为HQL的:username或是SQL的?号
```

## `SQL注入攻击的防范` ##
**问题:**
拼接的SQL如果不对单引号(有些数据库有反斜杠)进行过滤,则会存在SQL注入攻击问题

**解决:**
使用SafeSqlProcesser,进行sql过滤
```
XsqlBuilder builder = new XsqlBuilder(SafeSqlProcesserFactory.getMysql());
```



### SafeSqlProcesser其中的一个源码分析 ###
```
public class EscapeSingleQuotesSafeSqlProcesser implements SafeSqlProcesser{
	public String process(String value) {
		if(value == null) return null;
		return value.replaceAll("'", "''");
	}

}
```