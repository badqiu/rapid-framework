@echo 1. �������Դ��˲������ݿ�,����start_db.bat��������gen user_info���������ļ�
@echo 2. ����������Ҫ�����ļ�Ϊgenerator.xml,�����޸����ݿ���������
@echo 3. templateĿ¼Ϊ������������ģ��Ŀ¼,�����ɵ���ģ���Ŀ¼�ṹ



@set PATH=%JAVA_HOME%\bin;%PATH%;
@set classpath=%classpath%;.;.\lib\*;.\lib\rapid-generator.jar;.\lib\freemarker.jar;.\lib\h2-1.2.137.jar;.\lib\log4j-1.2.15.jar;.\lib\mysql-connector-java-5.0.5-bin.jar;.\lib\ojdbc14.jar;.\lib\postgresql-8.4-701.jdbc3.jar;.\lib\sqljdbc.jar
@java -jar lib\Main.jar
@if errorlevel 1 (
@echo ----------------------------------------------
@echo   ****����***: �����ú�JAVA_HOME�������������л��߼�����classpath·��
@pause
)

:end