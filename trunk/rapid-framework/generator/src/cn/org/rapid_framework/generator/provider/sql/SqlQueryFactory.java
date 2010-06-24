package cn.org.rapid_framework.generator.provider.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.provider.sql.model.ResultSetMetaDataHolder;
import cn.org.rapid_framework.generator.provider.sql.model.SelectParameter;
import cn.org.rapid_framework.generator.provider.sql.model.SelectSqlMetaData;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.NamedParameterUtils;
import cn.org.rapid_framework.generator.util.ParsedSql;
import cn.org.rapid_framework.generator.util.SqlParseHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 
 * 数据SQL查询语句生成SelectSqlMetaData对象,用于代码生成器的生成
 * 
 * @author badqiu
 *
 */
public class SqlQueryFactory {
    
    public SelectSqlMetaData getByQuery(String sourceSql) throws Exception {
        System.out.println("\n*******************************");
        System.out.println(" sql:"+sourceSql);
        System.out.println("*********************************");
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sourceSql);
        String sql = NamedParameterUtils.substituteNamedParameters(parsedSql);
        
        Connection conn = DbTableFactory.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        setPreparedStatementParameters(sql, ps);
        SelectSqlMetaData result = convert2SelectSqlMetaData(executeQueryForMetaData(ps)); 
        result.setSourceSql(sourceSql);
        result.setParams(parseSqlParameters(ps, parsedSql,result));
        return result;
    }

	private SelectSqlMetaData convert2SelectSqlMetaData(ResultSetMetaData metadata) throws SQLException, Exception {
		SelectSqlMetaData result = new SelectSqlMetaData();
        for(int i = 1; i <= metadata.getColumnCount(); i++) {
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
                result.addColumn(column);
            }else {
                Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnNameOrLabel(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
                result.addColumn(column);
                GLogger.debug("not found on table by table emtpty:"+BeanHelper.describe(column));
            }
        }
		return result;
	}

	private ResultSetMetaData executeQueryForMetaData(PreparedStatement ps)
			throws SQLException {
		ps.setMaxRows(3);
        ps.setFetchSize(3);
		ResultSetMetaData metadata =  null;
        try {
			ResultSet rs = ps.executeQuery();
			metadata = rs.getMetaData(); 
		} catch (Exception e) {
			metadata = ps.getMetaData();
		}
		return metadata;
	}

	private void setPreparedStatementParameters(String sql, PreparedStatement ps)
			throws SQLException {
        if(sql.contains("?")) {
            int size = sql.split("\\?").length;
            for(int i = 1; i <= size; i++) {
            	try {
            		ps.setInt(i,1);
            	}catch(Exception e) {
            		try {
            			ps.setString(i,"1");
            		}catch(Exception ee) {
            			System.err.println("error on set parametet index:"+i+" cause:"+ee);
            		}
            	}
            }
        }
	}

	private List<SelectParameter> parseSqlParameters(PreparedStatement ps,ParsedSql sql,SelectSqlMetaData sqlMetaData) throws Exception {

		List result = new ArrayList();
		for(int i = 0; i < sql.getParameterNames().size(); i++) {
			SelectParameter param = new SelectParameter();
			String paramName = sql.getParameterNames().get(i);
			param.setParamName(paramName);
			Column column = findColumnByParamName(sql, sqlMetaData, paramName);
			if(column == null) {
				param.setParameterClassName("String"); //FIXME 未设置正确的数据类型
			}else {
				param.setParameterClassName(column.getJavaType());
				param.setParameterType(column.getSqlType());
				param.setPrecision(column.getDecimalDigits());
				param.setScale(column.getSize());
				param.setParameterTypeName(column.getJdbcSqlTypeName());
			}
			result.add(param);			
		}
		return result;
    
	}

	private Column findColumnByParamName(ParsedSql sql,SelectSqlMetaData sqlMetaData, String paramName) throws Exception {
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
    	SelectSqlMetaData n2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=:username and password =:password");
    	SelectSqlMetaData n3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	SelectSqlMetaData n4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	SelectSqlMetaData n5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	SelectSqlMetaData n6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password and birth_date between :birthDateBegin and :birthDateEnd limit :offset,:limit");
    	SelectSqlMetaData n7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    	SelectSqlMetaData n8 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = :userId group by username");
    }
    
}
