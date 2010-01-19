package cn.org.rapid_framework.jdbc.sqlgenerator;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;

/**
 * 根据Table对象生成增删改查sql工具类.
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
	 * 联合主键: UPDATE user SET (user_id = :userId,user_name = :userName ) WHERE user_id = :user_id AND group_id = :group_id
	 * </pre>
	 */
	public String getUpdateByPkSql();

	/**
	 * 
	 * 单主键的表,使用固定的"?"来作为参数,联合主键则使用各自的column propertyName作为命名参数
	 * 
	 * <pre>
	 * 单主键: DELETE FROM user WHERE user_id = ?
	 * 联合主键: DELETE FROM user WHERE user_id = :user_id AND group_id = :group_id
	 * </pre>
	 */
	public String getDeleteByPkSql();

	/**
	 * 单主键的表,使用固定的"?"来作为参数,联合主键则使用各自的column propertyName作为命名参数
	 * 
	 * <pre>
	 * 单主键: SELECT user_id userId,user_name userName FROM user WHERE user_id = ?
	 * 联合主键: SELECT user_id userId,user_name userName FROM user WHERE user_id = :userId AND group_id = :group_id
	 * </pre>
	 */
	public String getSelectByPkSql();

	/**
	 * 得到列的sql段用于其它sql的拼接查询
	 * @return user_id userId,user_name userName
	 */
	public String getColumnsSql();
}
