
step1:导入工程
a.建议安装eclipse的m2eclipse插件，然后import-exist maven project
b.若未安装m2eclipse,可执行根目录下tools/bin/eclipse.bat生成eclipse project,然后import项目

step2:启动H2测试数据库
执行tools/bin/start_h2_db.bat

step3:生成CRUD代码
执行tools/rapid-gen/start-gen-cmd.bat,在命令行输入	 gen user_info
(或者用start-gen-gui.bat启动Swing界面进行选择操作)

step4:编译部署Java工程
执行tools/bin/mvn_install.bat,然后启动jetty.bat

step5:导入flex工程，并构建
将flex-project里的flex-cairngorm3项目导入FlashBuilder4
右键-属性-flex构建路径
修改 输出文件夹:			..\..\src\main\webapp\bin
修改 输入文件夹的URL:		http://localhost:8080/项目名/bin

step6:测试
访问http://localhost:8080/项目名/bin/UserInfoIndex.html,可以测试CRUD功能

(有问题可以反馈至：hhlai1990@gmail.com)





