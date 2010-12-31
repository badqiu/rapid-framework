@echo 命令行使用: 
@echo gen dal [table_sql_name] ,根据数据库表的配置文件生成代码,(需要有xml配置文件)
@echo gen table [table_sql_name] 根据数据库表的生成代码,可以生成dalgen的配置文件(不需要xml配置文件)
@echo gen seq : 生成oracle sequence SeqDAO生成代码

@IF DEFINED modeCmdExecuted GOTO START
@mode con cols=120 lines=3000
@set modeCmdExecuted="true"
@title Rapid Generator --- %cd%

:START
mvn groovy:execute -DgeneratorConfigFile=gen_config.xml -DexecuteTarget=%1 -DgenInputCmd=%2  -errors
