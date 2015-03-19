# ant文件 #

  * build.xml : ant 构建脚本
  * build.properties : ant配置属性


# 使用 #
**1.环境变量配置**

默认build.xml依赖环境变量:TOMCAT\_HOME

需要依赖该变量是由于web项目需要依赖J2EE的jars,如servlet api. 你也可以修改此项以不使用TOMCAT\_HOME.

不过笔者一般是使用TOMCAT\_HOME

**2.属性修改**
  * dirs.java.src : java源代码的路径
  * dirs.test.src : java测试源代码的路径
  * lib.classpath : 编译时依赖的classpath

```
<path id="lib.classpath">
	<fileset dir="generator/lib">
		<include name="**/*.jar"/>
	</fileset>
	<fileset dir="${dir.src.web}/WEB-INF/lib">
		<include name="**/*.jar"/>
	</fileset>
        <!--此处就是tomcat.home的作用,你也可以修改,直接引用其它路径 -->
	<fileset dir="${tomcat.home}">
		<include name="lib/*.jar"/>
		<include name="common/lib/*.jar"/>
	</fileset>
</path>

<path id="dirs.java.src">
  	<pathelement path="src/javacommon"/>
  	<pathelement path="src/resources"/>
  	<pathelement path="java_src"/>
</path>

<path id="dirs.test.src">
  	<pathelement path="java_test"/>
</path>
```

# 运行 #
如上配置完成,运行war任务即可构建出war包

其它任务:
cc\_main : 一般用于持续集成工具CruiseControl的接入