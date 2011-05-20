
命令行命令运行说明:
	gen dal trade_base 根据trade_base表生成dal层的代码
	gen table trade_base 生成trade_base的dalgen配置文件
	gen seq 生成SeqDao的代码

示例生成的代码输出的位置:
	target/generator-output

配置文件:
	gen_config.xml 包含生成器的相关配置
	gen.groovy 生成器最终调用的groovy文件

生成的代码需要引用的jar包:
		<dependency>
			<groupId>com.alipay.common</groupId>
			<artifactId>alipay-common-dal-util</artifactId>
			<version>1.0</version>
		</dependency>