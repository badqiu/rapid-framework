package cn.org.rapid_framework.jdbc.sqlgenerator;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;

/**
 * 根据Table对象生成增删改查sql工具类.
 *
 * <h3>API使用</h3>
 * <pre>
 * //table为metadata类,根据该类的数据生成增删改查sql
 * Table table = new Table("user",new Column("user_id","userId",true),new Column("user_name","userName"));
 * SqlGenerator singleGenerator = new SpringNamedSqlGenerator(table);
 *
 * //sql的值为: INSERT user (user_id,user_name) VALUES (:userId,:userName)
 * String sql = singleGenerator.getInsertSql();
 * </pre>
 *
 * @see Table
 * @author badqiu
 *
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
	 * 联合主键: UPDATE user SET (user_id = :userId,user_name = :userName ) WHERE user_id = :userId AND group_id = :groupId
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
	
	/**
	 * 增加前缀,得到列的sql段用于其它sql的拼接查询
	 * @return prefix.user_id prefix.userId,prefix.user_name userName
	 */
	public String getColumnsSql(String columnPrefix);


	public Table getTable();
}
