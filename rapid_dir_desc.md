# 目录结构 #
```
  java_src                 #java源代码目录
        --i18n               #存放国际化的messages
        --spring             #存放spring的配置文件
  java_test
  generator           #代码生成器目录
        --src               #生成器的源代码
        --lib               #生成器依赖的jar
  template            #生成器的模板，代码生成器将扫描该目录及子目录的所有文件,后读取该目录的模板然后生成代码
  plugins             #插件目录,插件安装完该目录可以删除
        --build.xml         #用于安装插件的ant脚本，包含install_plugin命令以供安装插件
  web                 #web源代码目录
  build.xml           #ant构建脚本，用于为项目打包
  build.properties    #ant build.xml的配置文件
```