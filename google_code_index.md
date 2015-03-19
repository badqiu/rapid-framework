![http://rapid-framework.googlecode.com/svn/trunk/images/logo.png](http://rapid-framework.googlecode.com/svn/trunk/images/logo.png)
### 类似ruby on rails的web项目脚手架 ###

一个类似 **ruby on rails** 的java web快速开发脚手架,rapid-framework是一个以spring为核心的项目脚手架，框架将各个零散的框架(struts,strust2,springmvc,hibernate,ibatis,spring\_jdbc,flex)搭建好，并内置一个代码生成器，辅助项目开发,可以生成java的hibernat model,dao,manager,struts+struts2 action类,可以生成jsp的增删改查及列表页面

**[在线文档](http://code.google.com/p/rapid-framework/wiki/menu)**

请加入**[google-group](http://groups.google.com/group/rapid-framework)**参与讨论

## 项目定位 ##
本框架是类似appfuse,springside的框架,但定位与springside及appfuse不同,它们更像是一些代码的最佳实践,而rapid-framework则是应用于实际项目开发的脚手架, 并且代码也将优化至极致.使用脚手架3分钟即可以搭建出一个实际项目.


## 框架特性 ##
  * 内置一个基于数据库的代码生成器rapid-generator,极易进行二次开发
  * 现整个项目使用plugin结构，根据自身的需要可以随意搭配项目组合(如springmvc+ibatis,struts2+hibernate)
  * 自带插件包括： struts,struts2,springmvc,hibernate,ibatis,spring\_jdbc,flex,extjs
  * 基于方言(Dialect)的分页,适用于ibatis,spring\_jdbc
  * 集成动态构造sql的工具:rapid-xsqlbuilder
  * 集成javascript表单验证:rapid-validation
  * 集成分页组件:extremeTable列表分页组件,rapid-simpletable分页tag
  * 集成DBUnit及spring对数据库测试的支持
  * Java日期转换通过代码处理,与My97DatePicker集成
  * 内置最精简的ant构建脚本,简单修改即可使用
  * 公共类库友好的包名javacommon
  * 整个项目尽量做到最小集,无需删除任何资源,拿来即可使用.
  * 完整的单元测试,保证代码质量
  * 友好的MIT-Licence


代码生成器生成的[增删改查截图](http://code.google.com/p/rapid-framework/wiki/snapshot)

后续请查看v2.x路线图: [roadmap](roadmap.md)

[bug提交](http://code.google.com/p/rapid-framework/issues/list)