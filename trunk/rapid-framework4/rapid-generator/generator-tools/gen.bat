@echo off
echo ----------------------------------------------------------------------------
echo ������ʹ��: 
echo gen gen [table_sql_name] ,�������ݿ��������ļ����ɴ���,(��Ҫ��xml�����ļ�)
echo gen table [table_sql_name] �������ݿ������ɴ���,��������dalgen�������ļ�(����Ҫxml�����ļ�)
echo gen seq : ����oracle sequence SeqDAO���ɴ���

mvn package -Dtarget=%1 -DgenInputCmd=%2