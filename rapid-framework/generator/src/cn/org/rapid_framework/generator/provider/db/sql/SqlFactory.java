package cn.org.rapid_framework.generator.provider.db.sql;

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

import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
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
    
	public static Sql parseSql(String sourceSql) {
		try {
			return new SqlFactory().parseSql0(sourceSql);
		}catch(Exception e) {
			throw new RuntimeException("parse sql error:"+sourceSql,e);
		}
	}
	
    public Sql parseSql0(String sourceSql) throws Exception {
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sourceSql);
        String executeSql = NamedParameterUtils.substituteNamedParameters(parsedSql);
        
        Sql sql = new Sql();
        sql.setExecuteSql(executeSql);
        sql.setSourceSql(sourceSql);
        System.out.println("\n*******************************");
        System.out.println("sourceSql  :"+sql.getSourceSql());
        System.out.println("executeSql :"+sql.getExecuteSql());
        System.out.println("*********************************");
        
        Connection conn = TableFactory.getInstance().getConnection();
        conn.setAutoCommit(false);
        conn.setReadOnly(true);
        try {
	        PreparedStatement ps = conn.prepareStatement(executeSql);
	        sql.setParams(parseForSqlParameters(parsedSql,sql));
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
		    Table table = TableFactory.getInstance().getTable(m.getTableName());
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
		SqlParseHelper.setRandomParamsValueForPreparedStatement(executeSql, ps);
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

	private List<SqlParameter> parseForSqlParameters(ParsedSql parsedSql,Sql sql) throws Exception {
		List<SqlParameter> result = new ArrayList<SqlParameter>();
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
			Table t = TableFactory.getInstance().getTable(tableName);
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
