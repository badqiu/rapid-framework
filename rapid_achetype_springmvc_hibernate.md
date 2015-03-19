# rapid-springmvc-hibernate的项目搭建 #
## 以下wiki内容处于测试阶段，欢迎大家提交bug ): ##

## Step1:从远程仓库创建你的项目 ##
```
mvn archetype:generate -B -DarchetypeGroupId=com.googlecode.rapid-framework  -DarchetypeArtifactId=rapid-springmvc-hibernate -DarchetypeVersion=1.0 -DarchetypeRepository=http://rapid-framework.googlecode.com/svn/trunk/repository -DgroupId=com.company.project -DartifactId=springmvc-hibernate-demo -Dversion=1.0
```
可以修改 groupId,artifactId,version参数来符合你的要求

## Step2:获取生成项目的jar包并install到本地 ##
```
mvn clean install 
```
如果是第一次使用rapid的maven版本可能需要下载一段时间，正式发布后我们会开放独立的jar包下载

## Step3:启动H2数据库，生成代码 ##
  1. :执行 **h2/start-db.bat** 启动测试数据库
  1. :在springmvc-hibernate-demo目录下分别执行如下命令
```
mvn rapid:print
mvn rapid:gen -Dtable=user_info
```
以上命令分别是打印当前数据库的所有表名以及从user\_info表生成模板代码.
### 然后将生成的src文件夹copy到springmvc-hibernate-demo中选择合并. ###

## Step4:运行并测试整个工程 ##
在springmvc-hibernate-demo目录下执行
```
mvn jetty:run
```
若服务器顺利启动，在浏览器输入http://localhost:8080/springmvc-hibernate-demo/pages/userinfo/  测试CRUD功能

## 导入项目到eclipse以及自定义数据库 ##
在springmvc-hibernate-demo目录下执行
eclipse.bat
然后将生成的eclipse工程导入到IDE
### 修改src\main\resources下的generator.xml以及spring/applicationContext-hibernate-dao.xml可以切换其他数据库，template为模板目录。 ###

### 注意事项 ###
当前内容属于提前测试内容，我们会陆续发布其他archetype，希望大家积极提交bug。如果您有啥改进意见请[mailto:hhlai1990@gmail.com](mailto:hhlai1990@gmail.com)

### 常见问题 ###
#### 1.如何切换oracle数据库? ####
现在的rapid-plugin提供命令行代码生成功能，默认只包含h2和mysql的数据库驱动，若要使用oracle，请在你的工程的pom.xml里修改plugins配置:
```
<!-- rapid-plugins -->
			<plugin>
				<groupId>com.googlecode.rapid-framework</groupId>
				<artifactId>rapid-plugins</artifactId>
				<version>${rapid.version}</version>
				<dependencies>
					<dependency>
						<groupId>com.oracle</groupId>
						<artifactId>ojdbc14</artifactId>
						<version>10.2.0.2.0</version>
					</dependency>
				</dependencies>
			</plugin>
```
然后修改dependencies配置，添加oracle依赖：
```
	        <dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc14</artifactId>
			<version>10.2.0.2.0</version>
		</dependency>
```