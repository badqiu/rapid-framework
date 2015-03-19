![http://rapid-framework.googlecode.com/svn/trunk/images/logo.png](http://rapid-framework.googlecode.com/svn/trunk/images/logo.png)
### web项目脚手架 ###

rapid-framework是一个以spring为核心的项目脚手架(或者称为胶水框架)，框架将各个零散的框架(struts,strust2,springmvc,hibernate,ibatis,spring\_jdbc,flex)搭建好，并内置一个代码生成器，辅助项目开发,可以生成java的hibernat model,dao,manager,struts+struts2 action类,可以生成jsp的增删改查及列表页面

**[在线文档](http://code.google.com/p/rapid-framework/wiki/menu)**

请加入**[google-group](http://groups.google.hk/group/rapid-framework)**参与讨论

**[rapid-framework官方网站](http://www.rapid-framework.org.cn)**

## 项目定位 ##
本框架是类似appfuse,springside的框架,但定位与springside及appfuse不同,它们更像是一些代码的最佳实践,而rapid-framework则是应用于实际项目开发的脚手架, 并且代码也将优化至极致.使用脚手架3分钟即可以搭建出一个实际项目.

## 框架概览 ##
  * **Dao:** spring\_jdbc, hibernate, ibatis2, ibatis3, 可以切换dao组件
  * **Dao Helper:** 基于dialect的分页(ibatis2, ibatis3, jdbc),动态sql构造工具rapid-xsqlbuilder
  * **Web MVC:** struts1, struts2, springmvc, springmvc\_rest, 可以切换web组件
  * **Template:** (velocity, freemarker, jsp)的模板可以相互继承, velocity及freemarker可以实现模板之间的管道操作
  * **分页组件:** extremeTable, rapid-simpletable
  * **UI:** rapid-validation表单验证, My97DatePicker日期控件
  * **单元测试:** spring test+DBunit
  * **底层支撑:** 插件体系及代码生成器(rapid-generator)

## 项目质量 ##
  * 完整的单元测试及持续集成,保证代码质量

代码生成器生成的[增删改查截图](http://code.google.com/p/rapid-framework/wiki/snapshot)

后续请查看v2.x路线图: [roadmap](roadmap.md)

[bug提交](http://code.google.com/p/rapid-framework/issues/list)


## Maven ##
如果你是maven用户,现在可以直接在maven官方仓库中引用到rapid的类库

```
<!-- rapid核心框架 -->
<dependency>
  <groupId>com.googlecode.rapid-framework</groupId>
  <artifactId>rapid-core</artifactId>
  <version>4.0</version>	
</dependency>
```

rapid-generator
```
<!-- 代码生成器核心引擎 -->
<dependency>
  <groupId>com.googlecode.rapid-framework</groupId>
  <artifactId>rapid-generator</artifactId>
  <version>4.0</version>	
</dependency>

<!-- 代码生成器扩展包 -->
<dependency>
  <groupId>com.googlecode.rapid-framework</groupId>
  <artifactId>rapid-generator-ext</artifactId>
  <version>4.0</version>	
</dependency>

<!-- 代码生成器模板,模板根目录通过 classpath:generator/template/rapid 可以引用 -->
<dependency>
  <groupId>com.googlecode.rapid-framework</groupId>
  <artifactId>rapid-generator-template</artifactId>
  <version>4.0</version>	
</dependency>

```
