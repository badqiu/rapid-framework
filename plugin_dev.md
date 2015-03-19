

# 插件开发手册 #

## 插件安装器为插件提供如下服务 ##
  * 拷贝插件目录下的内容至项目根目录
  * 将插件目录下的web\_merge.xml合并至项目的web.xml


所以依照上面提供的服务,我们只需将**插件的目录结构与项目的目录结构保持一致**,并且对需要在web.xml中插入配置内容的编写 **web\_merge.xml** 文件即可.

以下为插件编写介绍

---

# 插件的目录结构 #
```
plugin_name
	--java_src			#java源代码目录
	      --spring    			#存放spring配置文件,框架会自动加载该目录的xml文件 
        --java_test             #java测试源代码
	--web			#web源代码目录
	--template		#代码生成器的模板目录
	   --java_src	           #模板存放java源代码的地方
	   --java_test	           #模板存放java测试代码的目录
	   --web                   #模板存放web内容的地方
	--plugin_doc	        #插件的文档,可以放置插件安装的说明,该目录不会被拷贝至根目录
	web_merge.xml	        #等待合并至web.xml的内容.
```


---

# web\_merge.xml文件编写 #
插件安装器会将 **web\_merge.xml** 的内容插入在项目web.xml的
```
</web-app>
```
最后元素之前.
  * web\_merge.xml存放位置:放在插件的子目录下,如:${plugin\_name}/web\_merge.xml

## web.xml例子 ##
#### web.xml的在合并之前 ####
```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.4"
		 xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">


</web-app>
```

#### hibernate插件hibernate/web\_merge.xml的内容. ####
```
	<!--Hibernate Open Session in View Filter-->
	<filter>
		<filter-name>openSessionInViewFilter</filter-name>
		<filter-class>org.springframework.orm.hibernate3.support.OpenSessionInViewFilter</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>openSessionInViewFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```

#### 合并后web.xml的内容 ####
```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.4"
		 xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">

	<!--Hibernate Open Session in View Filter-->
	<filter>
		<filter-name>openSessionInViewFilter</filter-name>
		<filter-class>org.springframework.orm.hibernate3.support.OpenSessionInViewFilter</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>openSessionInViewFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

</web-app>
```


---

# 实际struts的插件目录结构例子 #
![http://rapid-framework.googlecode.com/svn/trunk/images/plugin/plugin_structure.jpg](http://rapid-framework.googlecode.com/svn/trunk/images/plugin/plugin_structure.jpg)