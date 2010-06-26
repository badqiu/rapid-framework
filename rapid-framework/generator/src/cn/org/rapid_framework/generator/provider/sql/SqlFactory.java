package cn.org.rapid_framework.generator.provider.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.provider.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.NamedParameterUtils;
import cn.org.rapid_framework.generator.util.sqlparse.ParsedSql;
import cn.org.rapid_framework.generator.util.sqlparse.ResultSetMetaDataHolder;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
/**
 * 
 * 根据SQL语句生成Sql对象,用于代码生成器的生成
 * 
 * @author badqiu
 *
 */
public class SqlFactory {
    
    public Sql parseSql(String sourceSql) throws Exception {
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sourceSql);
        String executeSql = NamedParameterUtils.substituteNamedParameters(parsedSql);
        
        Sql sql = new Sql();
        sql.setExecuteSql(executeSql);
        sql.setSourceSql(sourceSql);
        System.out.println("\n*******************************");
        System.out.println("sourceSql  :"+sql.getSourceSql());
        System.out.println("executeSql :"+sql.getExecuteSql());
        System.out.println("*********************************");
        
        Connection conn = DbTableFactory.getInstance().getConnection();
        conn.setAutoCommit(false);
        conn.setReadOnly(true);
        try {
	        PreparedStatement ps = conn.prepareStatement(executeSql);
	        sql.setParams(parseSqlParameters(parsedSql,sql));
	        sql.setColumns(convert2Columns(executeForResultSetMetaData(executeSql,ps)));
	        return sql;
        }finally {
        	conn.rollback();
        	conn.close();
        }
    }

	private LinkedHashSet<Column> convert2Columns(ResultSetMetaData metadata) throws SQLException, Exception {
		if(metadata == null) return new LinkedHashSet();
		LinkedHashSet<Column> columns = new LinkedHashSet();
        for(int i = 1; i <= metadata.getColumnCount(); i++) {
        	columns.add(convert2Column(metadata, i));
        }
		return columns;
	}

	private Column convert2Column(ResultSetMetaData metadata, int i) throws SQLException, Exception {
		ResultSetMetaDataHolder m = new ResultSetMetaDataHolder(metadata, i);
		if(StringHelper.isNotBlank(m.getTableName())) {
		    Table table = DbTableFactory.getInstance().getTable(m.getTableName());
		    Column column = table.getColumnBySqlName(m.getColumnNameOrLabel());
		    if(column == null) {
		        //可以再尝试解析sql得到 column以解决 password as pwd找不到column问题
		    	//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
		        column = new Column(table,m.getColumnType(),m.getColumnTypeName(),m.getColumnNameOrLabel(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
		        GLogger.debug("not found column:"+m.getColumnNameOrLabel()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
		        //isInSameTable以此种判断为错误
		    }else {
		    	GLogger.debug("found column:"+m.getColumnNameOrLabel()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
		    }
		    return column;
		}else {
		    Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnNameOrLabel(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
		    GLogger.debug("not found on table by table emtpty:"+BeanHelper.describe(column));
		    return column;
		}
	}

	private ResultSetMetaData executeForResultSetMetaData(String executeSql,PreparedStatement ps)throws SQLException {
		setPreparedStatementParameters(executeSql, ps);
		ps.setMaxRows(3);
        ps.setFetchSize(3);
        ps.setQueryTimeout(20);
        try {
			ResultSet rs = ps.executeQuery();
			return rs.getMetaData(); 
		} catch (Exception e) {
			return ps.getMetaData();
		}
	}

	private static void setPreparedStatementParameters(String sql, PreparedStatement ps) throws SQLException {
        if(sql.contains("?")) {
            int size = StringHelper.containsCount(sql,"?");
            for(int i = 1; i <= size; i++) {
            	long random = new Random(System.currentTimeMillis()).nextInt()+System.currentTimeMillis()+ new Random(System.currentTimeMillis()).nextInt();
				try {
            		ps.setLong(i, random);
            	}catch(SQLException e) {
            		try {
            			ps.setInt(i, (int)random);
            		}catch(SQLException e1) {
            			try {
                			ps.setString(i,""+random);
                		}catch(SQLException e2) {
                			try {
                				ps.setTimestamp(i,new java.sql.Timestamp(random));
                			}catch(SQLException e3) {
                				try {
                					ps.setDate(i,new java.sql.Date(random));
                    			}catch(SQLException e6) {
                    				try {
                    					ps.setObject(i, ""+(int)random);
                    				}catch(SQLException e4) {
                    					try {
                        					ps.setObject(i, ""+(short)random);
                        				}catch(SQLException e82) {
                        					try {
                            					ps.setObject(i, ""+(byte)random);
                            				}catch(SQLException error) {
                            					GLogger.warn("error on set parametet index:"+i+" cause:"+error+" sql:"+sql);
                            				}
                        				}
                    				}
                    			}
                			}
                		}            			
            		}
            		
            	}
            }
        }
	}

	private List<SqlParameter> parseSqlParameters(ParsedSql parsedSql,Sql sql) throws Exception {

		List result = new ArrayList();
		for(int i = 0; i < parsedSql.getParameterNames().size(); i++) {
			SqlParameter param = new SqlParameter();
			String paramName = parsedSql.getParameterNames().get(i);
			param.setParamName(paramName);
			Column column = findColumnByParamName(parsedSql, sql, paramName);
			if(column == null) {
				param.setParameterClassName("String"); //FIXME 未设置正确的数据类型
			}else {
				param.setParameterClassName(column.getJavaType());
				param.setParameterType(column.getSqlType());
				param.setPrecision(column.getDecimalDigits());
				param.setScale(column.getSize());
				param.setParameterTypeName(column.getJdbcSqlTypeName());
			}
			if(sql.getSourceSql().indexOf("(:"+paramName+")") >= 0) {
				param.setListParam(true);
			}
			result.add(param);			
		}
		return result;
    
	}

	private Column findColumnByParamName(ParsedSql sql,Sql sqlMetaData, String paramName) throws Exception {
		Column column = sqlMetaData.getColumnByName(paramName);
		if(column == null) {
			column = findColumnByParseSql(sql, paramName);
		}
		return column;
	}

	private Column findColumnByParseSql(ParsedSql sql, String paramName) throws Exception {
		Collection<String> tableNames = SqlParseHelper.getTableNamesByQuery(sql.toString());
		for(String tableName : tableNames) {
			Table t = DbTableFactory.getInstance().getTable(tableName);
			if(t != null) {
				Column column = t.getColumnByName(paramName);
				if(column != null) {
					return column;
				}
			}
		}
		return null;
	}
    
    public static void main(String[] args) throws Exception {
    	// ? parameters
//    	SelectSqlMetaData t1 = new SqlQueryFactory().getByQuery("select * from user_info");
//    	SelectSqlMetaData t2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=? and password =?");
//    	SelectSqlMetaData t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =? limit ?,?");
//    	SelectSqlMetaData t7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
//    
    	Sql n2 = new SqlFactory().parseSql("select user_info.username,password pwd from user_info where username=:username and password =:password");
    	Sql n3 = new SqlFactory().parseSql("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n4 = new SqlFactory().parseSql("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n5 = new SqlFactory().parseSql("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n6 = new SqlFactory().parseSql("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password and birth_date between :birthDateBegin and :birthDateEnd limit :offset,:limit");
    	Sql n7 = new SqlFactory().parseSql("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    	Sql n8 = new SqlFactory().parseSql("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = :userId group by username");
    }
    
}
