解压后,请自行补充web-inf\lib下的jar包
请自行下载jetty,将根目录lib下的jar补齐
请自行下载ext-2.2.1 置于web\widget\ext-2.2.1下


项目目录说明



conf		jetty配置
db_script 	生成数据库脚本
design		需求与ER图等
generator	生成器
lib		jetty的运行库
logs		jetty日志
plugins		脚手架插件
src		源码
template	模板 
web		WEB目录
start_jetty.bat	jetty启动


.如何开发
一般在第一次加载了脚手架以后,则每次增加模板运行生成器

.如何修改或增加多个web应用

修改etc\jetty.xml中Configure a WebApp部分,将路径和context指定为自己的应用
如需添加多个应用,再New一个配置.

.如何调试
本项目集成jetty服务,启动后,使用eclipse连接到运程调试端口4000即可