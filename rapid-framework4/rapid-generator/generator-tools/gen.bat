@echo off
echo ----------------------------------------------------------------------------
echo 命令行使用: 
echo gen gen [table_sql_name] ,根据数据库表的配置文件生成代码,(需要有xml配置文件)
echo gen table [table_sql_name] 根据数据库表的生成代码,可以生成dalgen的配置文件(不需要xml配置文件)
echo gen seq : 生成oracle sequence SeqDAO生成代码

mvn package -Dtarget=%1 -DgenInputCmd=%2