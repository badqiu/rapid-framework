

# 介绍 #

用于spring jdbc根据配置**生成**基本的**增删改查SQL**

与spring jdbc的命名参数配合使用,进一步精简dao的实现

# Sql生成接口声明 #
```
/**
 * 根据Table对象生成增删改查sql工具类.
 */
public interface SqlGenerator {
	/**
	 * @return INSERT user (user_id,user_name,pwd ) VALUES ( :userId,:userName,:pwd )
	 */
	public String getInsertSql();

	/**
	 * 单主键的表,使用固定的"?"来作为参数,联合主键则使用各自的column propertyName作为命名参数
	 * 
	 * <pre>
	 * 单主键: UPDATE user SET (user_id = :userId,user_name = :userName ) WHERE user_id = ?
	 * 联合主键: UPDATE user SET (user_id = :userId,user_name = :userName ) WHERE user_id = :userId AND group_id = :group_id
	 * </pre>
	 */
	public String getUpdateByPkSql();

	/**
	 * 
	 * 单主键的表,使用固定的"?"来作为参数,联合主键则使用各自的column propertyName作为命名参数
	 * 
	 * <pre>
	 * 单主键: DELETE FROM user WHERE user_id = ?
	 * 联合主键: DELETE FROM user WHERE user_id = :userId AND group_id = :groupId
	 * </pre>
	 */
	public String getDeleteByPkSql();

	/**
	 * 单主键的表,使用固定的"?"来作为参数,联合主键则使用各自的column propertyName作为命名参数
	 * 
	 * <pre>
	 * 单主键: SELECT user_id userId,user_name userName FROM user WHERE user_id = ?
	 * 联合主键: SELECT user_id userId,user_name userName FROM user WHERE user_id = :userId AND group_id = :groupId
	 * </pre>
	 */
	public String getSelectByPkSql();

	/**
	 * 得到列的sql段用于其它sql的拼接查询
	 * @return user_id userId,user_name userName
	 */
	public String getColumnsSql();
}
```

# API使用 #

```
 //table为metadata类,根据该类的数据生成增删改查sql
 Table table = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"));
 SqlGenerator singleGenerator = new SpringNamedSqlGenerator(table);
 
 //sql的值为: INSERT user (user_id,user_name) VALUES (:userId,:userName)
 String sql = singleGenerator.getInsertSql();
```

还有一个工具类可以创建Table对象.
```
Table t = MetadataCreateUtils.createTable(User.class);
```

# 使用JPA annotation #
User.class可以使用[JPA](http://java.sun.com/javaee/technologies/persistence.jsp)的annotation,现只支持@Table,@ID,@Column,@Transient,示例如下:
```
@Table(name="ann_user")
public class User {
	private String userName;
	private int age;
	@Id
	@Column(name="ann_id")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="ann_age")
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
```