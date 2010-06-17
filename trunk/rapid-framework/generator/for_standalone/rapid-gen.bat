@echo 1. 生成器自带了测试数据库,运行start_db.bat后再运行gen user_info即可生成文件
@echo 2. 生成器的主要配置文件为generator.properties,里面修改数据库连接属性
@echo 3. template目录为代码生成器的模板目录,可自由调整模板的目录结构

@if not defined JAVA_HOMEA goto noJavaHome
@set JAVA_PATH="%JAVA_HOME%\bin\java"
@%JAVA_PATH% -server -Xms128m -Xmx384m -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine template
goto end

:noJavaHome
@java -server -Xms128m -Xmx384m -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine template
@if errorlevel 1 (
@echo ----------------------------------------------
@echo   ****错误***: 请设置好JAVA_HOME环境变量再运行
@pause
)

:end