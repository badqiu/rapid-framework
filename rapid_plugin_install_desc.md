

# 插件安装使用说明 #
```
plugins        所有插件存放目录
   struts2     struts2插件
   hibernate   hibernate插件
   ...
   --build.xml 插件安装ant脚本,用于安装插件
```
现所有的插件存放在plugins目录,plugins目录包含一个ant脚本build.xml,通过运行该ant脚本来安装插件.

# 通过ant插件安装 #
通过eclipse打开文件,再打开outline视图,右键点击target即可运行ant任务
![http://rapid-framework.googlecode.com/svn/trunk/images/doc/struts2_hibernate_step_by_step/run_plugin_installer.jpg](http://rapid-framework.googlecode.com/svn/trunk/images/doc/struts2_hibernate_step_by_step/run_plugin_installer.jpg)

## ant任务介绍 ##
  * install\_plugin : 弹出对话框根据"插件名称"安装插件.可以安装任何插件
  * install\_xxxxxx : 内置的几个名称用于快速插件安装,避免需要手工输入插件名称.
> 如需要安装struts2与hibernate插件,直接运行install\_struts2+hibernate target即可

# 插件安装完的处理 #
> 插件安装完,如果其它插件你也不需要,可以将所有插件删除,直接删除plugins目录即可

# 手工安装插件 #
如果ant运行有问题,你可以手工安装插件.
因为ant脚本实现也只实现两个功能
  * 拷贝插件目录下的内容至项目根目录
  * 将插件目录下的web\_merge.xml合并至项目的web.xml的尾部

所以你手工执行上面步骤即可