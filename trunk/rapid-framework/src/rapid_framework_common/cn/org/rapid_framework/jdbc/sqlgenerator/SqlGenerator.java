package cn.org.rapid_framework.jdbc.sqlgenerator;

/**
 * 根据table映射生成增删改查的sql.
 * 
 * 
 * @author badqiu
 *
 */
public interface SqlGenerator {
	/**
	 * @return INSERT user (user_id,user_name,pwd ) VALUES ( :userId,:userName,:pwd ) 
	 */
	public String getInsertSql();
	
	/**
	 * 用于单主键的表,使用固定的":id"来作为命名参数
	 * @return UPDATE user SET (user_id = :userId,user_name = :userName,pwd = :pwd ) WHERE user_id = :id
	 */	
	public String getUpdateByPkSql();

	/**
	 * 用于单主键的表,使用固定的":id"来作为命名参数
	 * @return DELETE FROM user WHERE user_id = :id 
	 */
	public String getDeleteByPkSql();

	/**
	 * 用于单主键的表,使用固定的":id"来作为命名参数
	 * @return SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = :id
	 */
	public String getSelectByPkSql();
//	/**
//	 * @return UPDATE user SET (user_id = :userId,user_name = :userName,pwd = :pwd ) WHERE user_id = :userId
//	 */
//	public String getUpdateByMultiPkSql();
//	
//	/**
//	 * 用于单主键的表,使用固定的":id"来作为命名参数
//	 * @return UPDATE user SET (user_id = :userId,user_name = :userName,pwd = :pwd ) WHERE user_id = :id
//	 */
//	public String getUpdateBySinglePkSql();	
//	
//	/**
//	 * 用于联合主键
//	 * @return DELETE FROM user WHERE user_id = :userId AND group_id = :groupId
//	 */
//	public String getDeleteByMultiPkSql();
//	
//	/**
//	 * 用于单主键的表,使用固定的":id"来作为命名参数
//	 * @return DELETE FROM user WHERE user_id = :id 
//	 */
//	public String getDeleteBySinglePkSql();
//	
//	/**
//	 * 用于联合主键
//	 * @return SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = :userId AND group_id = :groupId
//	 */
//	public String getSelectByMultiPkSql();
//	
//	/**
//	 * 用于单主键的表,使用固定的":id"来作为命名参数
//	 * @return SELECT user_id userId,user_name userName,pwd pwd FROM user WHERE user_id = :id
//	 */
//	public String getSelectBySinglePkSql();
	
	/**
	 * 得到列的映射
	 * @return user_id userId,user_name userName,pwd pwd
	 */
	public String getColumnsSql();
}
