此页包含工具类简单介绍,具体使用请查阅**javadoc**

# JdbcPlaceholderConfigurer #

使spring的xml配置文件可以从数据库读取placeholder: ${xxxxx}的配置信息

# MultiThreadTestUtils #
多线程测试的工具类
```
//同时多线程的方式执行task
MultiThreadTestUtils.executeAndWait(int threadCount, java.lang.Runnable task).
```