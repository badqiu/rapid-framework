@echo ������ʹ��: 
@echo gen dal [table_sql_name] ,�������ݿ��������ļ����ɴ���,(��Ҫ��xml�����ļ�)
@echo gen table [table_sql_name] �������ݿ������ɴ���,��������dalgen�������ļ�(����Ҫxml�����ļ�)
@echo gen seq : ����oracle sequence SeqDAO���ɴ���

@IF DEFINED modeCmdExecuted GOTO START
@mode con cols=120 lines=3000
@set modeCmdExecuted="true"
@title Rapid Generator

:START
mvn groovy:execute -DgeneratorConfigFile=gen_config.xml -DexecuteTarget=%1 -DgenInputCmd=%2  -errors
