# rapid-framework概述 #

rapid-framework是一个以spring为核心的项目脚手架,以插件的方式将不同的开源项目集合在一起,并可以实现不同的项目组合.如(struts2+spring+hibernate),(struts2+spring+iBatis),(springmvc+spring+iBatis)的项目组合.

# 项目结构 #
项目的结构是经典的三层结构: Action => Service => Dao

## 框架概览 ##
  * **Dao:** spring\_jdbc, hibernate, ibatis2, ibatis3, 可以切换dao组件
  * **Dao Helper:** 基于dialect的分页(ibatis2, ibatis3, jdbc),动态sql构造工具rapid-xsqlbuilder
  * **Web MVC:** struts1, struts2, springmvc, springmvc\_rest, 可以切换web组件
  * **Template:** (velocity, freemarker, jsp)的模板可以相互继承, velocity及freemarker可以实现模板之间的管道操作
  * **分页组件:** extremeTable, rapid-simpletable
  * **UI:** rapid-validation表单验证, My97DatePicker日期控件
  * **单元测试:** spring test+DBunit
  * **底层支撑:** 插件体系及代码生成器(rapid-generator)