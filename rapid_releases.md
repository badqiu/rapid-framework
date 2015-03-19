rapid-framework发布v2.5.0

changelog:
rapid-framework v2.5.0
> 提升:
> XsqlBuilder支持Object作为filters
> PageRequest.filters使用范型

> 新特性:
> 支持Ibatis的分页Dialect
> 新增其它数据库的分页Dialect

> 生成器:
> 支持从数据库中读取注释作为alias

> 生成器模板:
> 删除subpackage,使用namespace变量替换,但只作用于jsp
> list.jsp现在包含查询

> 新增插件:
> simpletable 简单的列表分页
> extjs		ext模板

> 其它一些bugfixed及大量整理及重构


rapid-framework介绍:
rapid-framework是一个类似ruby on rails项目脚手架，框架将各个零散的框架(struts,strust2,springmvc,hibernate,ibatis,spring\_jdbc,flex)以插件的方式搭建好，根据自身的需要可以随意搭配项目组合(如springmvc+ibatis,struts2+hibernate)，并内置一个代码生成器，辅助项目开发,可以生成java的hibernat model,dao,manager,struts+struts2 action类,可以生成jsp的增删改查及列表页面

项目定位:
本框架是类似appfuse,springside的框架,但定位与springside及appfuse不同,它们更像是一些代码的最佳实践,而rapid-framework则是可以真正应用于项目开发, 并且代码也将优化至极致.使用脚手架3分钟即可以搭建出一个实际项目.

附语:
任何一个公司或是个人都应该有自己内部的一个脚手架，是公司的积累以及一致的开发标准，如果你现在仍没有，那么rapid-framework现在是你最适合的选择，可以自由扩展，构建属于自己的脚手架.

项目网站及下载: http://code.google.com/p/rapid-framework/

项目搭建请查看项目的在线文档.