@set PATH=%JAVA_HOME%\bin;%PATH%;
@java -cp "lib\h2-1.2.137.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %*
@if errorlevel 1 pause