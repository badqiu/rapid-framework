@echo 1. �������Դ��˲������ݿ�,����start_db.bat��������gen user_info���������ļ�
@echo 2. ����������Ҫ�����ļ�Ϊgenerator.properties,�����޸����ݿ���������
@echo 3. templateĿ¼Ϊ������������ģ��Ŀ¼,�����ɵ���ģ���Ŀ¼�ṹ

@set classpath=%classpath%;.;.\lib\*;.\lib\rapid-generator-3.9.0.20100617.jar;.\lib\freemarker.jar;.\lib\h2-1.2.137.jar;h2.jar;.\lib\log4j-1.2.15.jar;.\lib\mysql-connector-java-5.0.5-bin.jar;.\lib\ojdbc14.jar;.\lib\postgresql-8.4-701.jdbc3.jar;.\lib\sqljdbc.jar

@if not defined JAVA_HOMEA goto noJavaHome
@set JAVA_PATH="%JAVA_HOME%\bin\java"
@%JAVA_PATH% -server -Xms128m -Xmx384m cn.org.rapid_framework.generator.ext.CommandLine template
goto end

:noJavaHome
@java -server -Xms128m -Xmx384m cn.org.rapid_framework.generator.ext.CommandLine template
@if errorlevel 1 (
@echo ----------------------------------------------
@echo   ****����***: �����ú�JAVA_HOME�������������л��߼�����classpath·��
@pause
)

:end