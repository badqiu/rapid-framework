

# 开发环境配置 #

现整个项目基于maven构建. 所以在使用项目必须下载maven


## 1.maven安装 ##
  1. 下载maven http://maven.apache.org/ 并解压至本地目录,如c:/dev/mvn
  1. 设置系统环境变量: M2\_HOME=c:/dev/mvn
  1. 设置Path系统环境变量: path=%M2\_HOME%\bin;
  1. 测试mvn安装成功
```
mvn --version
```

## 2.checkout 项目 ##
  1. 将源代码从[source](source.md) 检出. 注意,要有commit权限必须是 https的URL


---

## 3.将项目导入 eclipse ##
  1. 然后在项目根目录命令行运行mvn eclipse命令. 请耐心等待下载相关jar包
```
mvn eclipse:eclipse
```
  1. 然后通过eclipse导入项目,选择菜单 File => Import... => Existing Project Into Workspace
  1. 配置eclipse M2\_REPO环境变量.


**命令行配置**


> 通过命令行配置eclipse M2\_REPO环境变量:
在命令行运行:
```
mvn eclipse:configure-workspace -Dworkspace=c:/youWorkspaceLocation
```

**手工配置**:


M2\_REPO就是mvn本地仓库的位置.选择菜单: Window => Perferences => Java => Build Path => Classpath Variables
![http://rapid-framework.googlecode.com/svn/trunk/images/doc/maven/eclipse_m2_var_config.jpg](http://rapid-framework.googlecode.com/svn/trunk/images/doc/maven/eclipse_m2_var_config.jpg)
注意: C:\.m2\repository是我自己手工修改 M2\_HOME/conf/settings.xml的配置。



---

## 4.项目构建 ##
进入项目根目录,运行命令就可以构建项目:
```
mvn install -Dmaven.test.skip=true
```
-Dmaven.test.skip=true意思是不运行单元测试. 构建出来的产品在你的本地mvn仓库. mvn groupId是com.googlecode.rapid-framework