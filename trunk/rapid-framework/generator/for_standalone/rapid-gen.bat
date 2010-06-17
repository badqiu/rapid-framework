@echo 1. 生成器自带了测试数据库,运行start_db.bat后再运行gen user_info即可生成文件
@echo 2. 生成器的主要配置文件为generator.properties,里面修改数据库连接属性
@echo 3. template目录为代码生成器的模板目录,可自由调整模板的目录结构

@set classpath=%classpath%;.;.\lib\*;.\lib\rapid-generator-3.9.0.20100617.jar;.\lib\freemarker.jar;.\lib\h2-1.2.137.jar;h2.jar;.\lib\log4j-1.2.15.jar;.\lib\mysql-connector-java-5.0.5-bin.jar;.\lib\ojdbc14.jar;.\lib\postgresql-8.4-701.jdbc3.jar;.\lib\sqljdbc.jar

@if not defined JAVA_HOMEA goto noJavaHome
@set JAVA_PATH="%JAVA_HOME%\bin\java"
@%JAVA_PATH% -server -Xms128m -Xmx384m cn.org.rapid_framework.generator.ext.CommandLine template
goto end

:noJavaHome
@java -server -Xms128m -Xmx384m cn.org.rapid_framework.generator.ext.CommandLine template
@if errorlevel 1 (
@echo ----------------------------------------------
@echo   ****错误***: 请设置好JAVA_HOME环境变量再运行
@pause
)

:end