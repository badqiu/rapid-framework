if not defined JAVA_HOMEA goto noJavaHome
set JAVA_PATH="%JAVA_HOME%\bin\java"
%JAVA_PATH% -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine
goto end

:noJavaHome
@echo "请设置好JAVA_HOME环境变量再运行"
java -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine
pause

:end