# 单元测试 #

实际的项目开发中，只需对Dao层与Service层进行单元测试即可.(注:现dao,service的测试是直接对数据库连接查询测试,也即为集成测试)
Action那一层不要包含过多逻辑，一般不进去单元测试.

而html页面的自动化测试工具(Selenium）一般项目实在很难做到。因为页面改动太频繁了。
# 数据库单元测试最佳实践 #
![http://rapid-framework.googlecode.com/svn/trunk/images/doc/test/db_test_best_practice.jpg](http://rapid-framework.googlecode.com/svn/trunk/images/doc/test/db_test_best_practice.jpg)

如上图描述，这样在编写测试逻辑时，插入数据库中的数据可以自动回滚，但插入测试又可以正常执行。
想要查询插入的数据是否成功也是可以查得到的.

# [DBUnit](http://www.dbunit.org/) #
是数据库单元测试的工具类，主要为数据库插入测试数据(现rapid使用rapid文件格式)非常方便，也可以将数据库测试数据导出功能

### dbunit xml数据样例如下 ###
```
<?xml version='1.0' encoding='UTF-8'?>
<!-- DBUnit flatXml DataFile -->
<dataset>

  <product 
	id='830' 
	platform='06' 
	category='901' 
	code='299' 
	name='Name689' 
	old_name='OldName549' 
	create='2008-01-01 23:20:20' 
	modified='2008-01-01 23:20:20' 
  />
  
</dataset>
```

具体查看BaseDaoTestCase.java,BaseManagerTestCase.java的代码.