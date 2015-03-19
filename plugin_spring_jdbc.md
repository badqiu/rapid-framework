# spring jdbc插件 #

**主要改进**
  1. 使用分页方言,请查看**[分页查询方言(dialect)](rapid_dialect.md)**
  1. jdbc sql生成器，可以为一表张生成基本的增删改查sql,具体请查看[这里](rapid_sqlgenerator.md)

jdbc的dao生成的代码如下(包含增删改查功能):

```
@Component
public class UserInfoDao extends BaseSpringJdbcDao<UserInfo,java.lang.Long>{
	
	//注意: getSqlGenerator()可以生成基本的：增删改查sql语句, 通过这个父类已经封装了增删改查操作
	public Class getEntityClass() {
		return UserInfo.class;
	}
	
	public void save(UserInfo entity) {
		String sql = getSqlGenerator().getInsertSql();
		insertWithIdentity(entity,sql); //for sqlserver and mysql
		
		//其它主键生成策略
		//insertWithOracleSequence(entity,"sequenceName",sql); //oracle sequence: 
		//insertWithDB2Sequence(entity,"sequenceName",sql); //db2 sequence:
		//insertWithUUID(entity,sql); //uuid
		//insertWithAssigned(entity,sql); //手工分配
	}
	
	public Page findByPageRequest(PageRequest<Map> pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// [column]为字符串拼接, {column}为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应用,使用占位符方式可以优化性能. 
		// [column] 为PageRequest.getFilters()中的key
		String sql = "select "+ getSqlGenerator().getColumnsSql("t") + " from user_info t where 1=1 "
				+ "/~ and t.password = '[password]' ~/"
				+ "/~ and t.user_name = '[userName]' ~/"
				+ "/~ and t.age = '[age]' ~/"
				+ "/~ and t.sex = '[sex]' ~/"
				+ "/~ order by [sortColumns] ~/";
		return pageQuery(sql,pageRequest);
	}
	

}
```