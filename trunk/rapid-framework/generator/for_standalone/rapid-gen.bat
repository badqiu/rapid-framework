@echo 1. �������Դ��˲������ݿ�,����start_db.bat��������gen user_info���������ļ�
@echo 2. ����������Ҫ�����ļ�Ϊgenerator.properties,�����޸����ݿ���������
@echo 3. templateĿ¼Ϊ������������ģ��Ŀ¼,�����ɵ���ģ���Ŀ¼�ṹ

@if not defined JAVA_HOMEA goto noJavaHome
@set JAVA_PATH="%JAVA_HOME%\bin\java"
@%JAVA_PATH% -server -Xms128m -Xmx384m -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine
goto end

:noJavaHome
@java -server -Xms128m -Xmx384m -cp .\lib\*;. cn.org.rapid_framework.generator.ext.CommandLine
@if errorlevel 1 (
@echo ----------------------------------------------
@echo   ****����***: �����ú�JAVA_HOME��������������
@pause
)

:end