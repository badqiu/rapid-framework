package cn.org.rapid_framework.generator.provider.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory.NotFoundTableException;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.DBHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlerrorcode.SQLErrorCodeSQLExceptionTranslator;
import cn.org.rapid_framework.generator.util.sqlparse.BasicSqlFormatter;
import cn.org.rapid_framework.generator.util.sqlparse.NamedParameterUtils;
import cn.org.rapid_framework.generator.util.sqlparse.ParsedSql;
import cn.org.rapid_framework.generator.util.sqlparse.ResultSetMetaDataHolder;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator.util.sqlparse.StatementCreatorUtils;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper.NameWithAlias;
import cn.org.rapid_framework.generator.util.typemapping.JdbcType;
/**
 * 
 * 根据SQL语句生成Sql对象,用于代码生成器的生成<br />
 * 
 * 示例使用：
 * <pre>
 * Sql sql = new SqlFactory().parseSql("select * from user_info where username=#username# and password=#password#");
 * </pre>
 * 
 * @author badqiu
 *
 */
public class SqlFactory {
    
    public SqlFactory() {
    }
    
    public Sql parseSql(String sourceSql) {
    	if(StringHelper.isBlank(sourceSql)) throw new IllegalArgumentException("sourceSql must be not empty");
    	String beforeProcessedSql = beforeParseSql(sourceSql);
    	
//    	String unscapedSourceSql = StringHelper.unescapeXml(beforeProcessedSql);
    	String namedSql = SqlParseHelper.convert2NamedParametersSql(beforeProcessedSql,":","");
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(namedSql);
        String executeSql = new BasicSqlFormatter().format(NamedParameterUtils.substituteNamedParameters(parsedSql));
        
        Sql sql = new Sql();
        sql.setSourceSql(sourceSql);
        sql.setExecuteSql(executeSql);
        GLogger.debug("\n*******************************");
        GLogger.debug("sourceSql  :"+sql.getSourceSql());
        GLogger.debug("namedSql  :"+namedSql);
        GLogger.debug("executeSql :"+sql.getExecuteSql());
        GLogger.debug("*********************************");
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
        	conn = DataSourceProvider.getNewConnection();
        	conn.setAutoCommit(false);
//        	if(DatabaseMetaDataUtils.isMysqlDataBase(conn.getMetaData())){
//        		conn.setReadOnly(true);
//        	}
	        ps = conn.prepareStatement(SqlParseHelper.removeOrders(executeSql));
	        
	        SqlParametersParser sqlParametersParser = new SqlParametersParser();
            sqlParametersParser.execute(parsedSql,sql);
            
	        ResultSetMetaData resultSetMetaData = executeSqlForResultSetMetaData(executeSql,ps,sqlParametersParser.allParams);
            sql.setColumns(new SelectColumnsParser().convert2Columns(sql,resultSetMetaData));
            sql.setParams(sqlParametersParser.params);
	        
	        return afterProcessedSql(sql);
        }catch(SQLException e) {
        	throw new RuntimeException("execute sql occer error,\nexecutedSql:"+SqlParseHelper.removeOrders(executeSql),e);
        }catch(Exception e) {
        	throw new RuntimeException("sql parse error,\nexecutedSql:"+SqlParseHelper.removeOrders(executeSql),e);
        }finally {
        	try {
	        	DBHelper.rollback(conn);
        	}finally {
        		DBHelper.close(conn,ps,null);
        	}
        }
    }

    protected Sql afterProcessedSql(Sql sql) {
		return sql;
	}

	protected String beforeParseSql(String sourceSql) {
		return sourceSql;
	}

    private ResultSetMetaData executeSqlForResultSetMetaData(String sql,PreparedStatement ps,List<SqlParameter> params)throws SQLException {
//		SqlParseHelper.setRandomParamsValueForPreparedStatement(SqlParseHelper.removeOrders(executeSql), ps);
		StatementCreatorUtils.setRandomParamsValueForPreparedStatement(sql, ps, params);
		
		try {
			ps.setMaxRows(3);
	        ps.setFetchSize(3);
	        ps.setQueryTimeout(20);
	        ResultSet rs = null;
			if(ps.execute()) {
				rs = ps.getResultSet();
				return rs.getMetaData();
			}
			return null;
		}catch(SQLException e) {
			if(isDataIntegrityViolationException(e)) {
			    GLogger.warn("ignore executeSqlForResultSetMetaData() SQLException,errorCode:"+e.getErrorCode()+" sqlState:"+e.getSQLState()+" message:"+e.getMessage()+ "\n executedSql:"+sql);
				return null;
			}
			String message = "errorCode:"+e.getErrorCode()+" SQLState:"+e.getSQLState()+" errorCodeTranslatorDataBaaseName:"+getErrorCodeTranslatorDataBaaseName()+" "+ e.getMessage();
			throw new SQLException(message,e.getSQLState(),e.getErrorCode());
		}
	}

    private String getErrorCodeTranslatorDataBaaseName() {
        SQLErrorCodeSQLExceptionTranslator transaltor = SQLErrorCodeSQLExceptionTranslator.getSQLErrorCodeSQLExceptionTranslator(DataSourceProvider.getDataSource());
        if(transaltor.getSqlErrorCodes() == null) return "null";
        return Arrays.toString(transaltor.getSqlErrorCodes().getDatabaseProductNames());
    }

    /** 判断是否是外键,完整性约束等异常 引发的异常 */
    protected boolean isDataIntegrityViolationException(SQLException sqlEx) {
        SQLErrorCodeSQLExceptionTranslator transaltor = SQLErrorCodeSQLExceptionTranslator.getSQLErrorCodeSQLExceptionTranslator(DataSourceProvider.getDataSource());
        return transaltor.isDataIntegrityViolation(sqlEx);
	}

	public static Map<String,Table> cache = new HashMap<String,Table>();
    public static Table getTableFromCache(String tableSqlName) {
        if(tableSqlName == null) throw new IllegalArgumentException("tableSqlName must be not null");
        
        Table table = cache.get(tableSqlName.toLowerCase());
        if(table == null) {
            table = TableFactory.getInstance().getTable(tableSqlName);
            cache.put(tableSqlName.toLowerCase(), table);
        }
        return table;
    }
	
    public static class SelectColumnsParser {
    
		private LinkedHashSet<Column> convert2Columns(Sql sql,ResultSetMetaData metadata) throws SQLException, Exception {
			if(metadata == null) return new LinkedHashSet();
			LinkedHashSet<Column> columns = new LinkedHashSet();
	        for(int i = 1; i <= metadata.getColumnCount(); i++) {
	        	Column c = convert2Column(sql,metadata, i);
	        	if(c == null) throw new IllegalStateException("column must be not null");
				columns.add(c);
	        }
			return columns;
		}
	
        private Column convert2Column(Sql sql,ResultSetMetaData metadata, int i) throws SQLException, Exception {
			ResultSetMetaDataHolder m = new ResultSetMetaDataHolder(metadata, i);
			if(StringHelper.isNotBlank(m.getTableName())) {
				//FIXME 如果表有别名,将会找不到表,如 inner join user_info t1, tableName将为t1,应该转换为user_info
				Table table = foundTableByTableNameOrTableAlias(sql, m.getTableName());
				if(table == null) {
					return newColumn(null,m);
				}
			    Column column = table.getColumnBySqlName(m.getColumnLabelOrName());
			    if(column == null || column.getSqlType() != m.getColumnType()) {
			        //可以再尝试解析sql得到 column以解决 password as pwd找不到column问题
			        column = newColumn(table,m);
			        GLogger.trace("not found column:"+m.getColumnLabelOrName()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
			        //isInSameTable以此种判断为错误
			    }else {
			    	GLogger.trace("found column:"+m.getColumnLabelOrName()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
			    }
			    return column;
			}else {
			    return newColumn(null,m);
			}
		}

		private Column newColumn(Table table,ResultSetMetaDataHolder m) {
			//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
			Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnLabelOrName(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
			GLogger.trace("not found on table by table emtpty:"+BeanHelper.describe(column));
			return column;
		}

		private Table foundTableByTableNameOrTableAlias(Sql sql,String tableNameId) throws Exception {
			try {
				return getTableFromCache(tableNameId);
			}catch(NotFoundTableException e) {
				Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery(sql.getExecuteSql());
				for(NameWithAlias tableName : tableNames) {
					if(tableName.getAlias().equalsIgnoreCase(tableNameId)) {
						return getTableFromCache(tableName.getName());
					}
				}
			}
			return null;
		}
    }

	public static class SqlParametersParser {
		private static Map<String,Column> specialParametersMapping = new HashMap<String,Column>();
		{
			specialParametersMapping.put("offset", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","offset",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("limit", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","limit",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageSize", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageSize",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageNo", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageNo",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageNumber", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageNumber",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageNum", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageNumber",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("page", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","page",0,0,false,false,false,false,null,null));
			
			specialParametersMapping.put("beginRow", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","beginRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("beginRows", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","beginRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("startRow", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","startRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("startRows", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","startRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("endRow", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","endRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("endRows", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","endRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("lastRow", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","lastRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("lastRows", new Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","lastRows",0,0,false,false,false,false,null,null));
			
			specialParametersMapping.put("orderBy", new Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","orderBy",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("orderby", new Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","orderby",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("sortColumns", new Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","sortColumns",0,0,false,false,false,false,null,null));
		}
		
		public LinkedHashSet<SqlParameter> params = new LinkedHashSet<SqlParameter>();
		public List<SqlParameter> allParams = new ArrayList<SqlParameter>();
		
		private void execute(ParsedSql parsedSql,Sql sql) throws Exception {
			long start = System.currentTimeMillis();
			for(int i = 0; i < parsedSql.getParameterNames().size(); i++) {
				String paramName = parsedSql.getParameterNames().get(i);
				Column column = findColumnByParamName(parsedSql, sql, paramName);
				if(column == null) {
					column = specialParametersMapping.get(paramName);
					if(column == null) {
						//FIXME 不能猜测的column类型
						column = new Column(null,JdbcType.UNDEFINED.TYPE_CODE,"UNDEFINED",paramName,0,0,false,false,false,false,null,null);
					}
				}
				SqlParameter param = new SqlParameter(column);
				
				param.setParamName(paramName);
				if(isMatchListParam(sql.getSourceSql(), paramName)) { //FIXME 只考虑(:username)未考虑(#inUsernames#) and (#{inPassword}),并且可以使用 #inUsername[]#
					param.setListParam(true);
				}
				params.add(param);
				allParams.add(param);
			}
			GLogger.perf("parseForSqlParameters() cost:"+(System.currentTimeMillis()- start));
		}

		public boolean isMatchListParam(String sql, String paramName) {
			return 
			    sql.matches("(?s).*\\sin\\s*\\([:#\\$&]\\{?"+paramName+"\\}?[$#}]?\\).*") // match in (:username) ,not in (#username#)
			    || sql.matches("(?s).*[#$]"+paramName+"\\[]\\.?\\w*[#$].*") //match #user[]# $user[]$ #user[].age# for ibatis
			   || sql.matches("(?s).*[#$]\\{"+paramName+"\\[[$\\{\\}\\w]+]\\}*.*"); //match #{user[index]}# ${user[${index}]}  for mybatis
		}
	
		private Column findColumnByParamName(ParsedSql parsedSql,Sql sql, String paramName) throws Exception {
			Column column = sql.getColumnByName(paramName);
			if(column == null) {
				//FIXME 还未处理 t.username = :username的t前缀问题,应该直接根据 t.确定属于那一张表,不需要再猜测
				String leftColumn = SqlParseHelper.getColumnNameByRightCondition(parsedSql.toString(), paramName);
				if(leftColumn != null) {
					column = findColumnByParseSql(parsedSql, leftColumn );
				}
			}
			if(column == null) {
				column = findColumnByParseSql(parsedSql, paramName);
			}
			return column;
		}
	
		private Column findColumnByParseSql(ParsedSql sql, String paramName) throws Exception {
			if(paramName == null) throw new NullPointerException("'paramName' must be not null");
			try {
    			Collection<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery(sql.toString());
    			for(NameWithAlias tableName : tableNames) {
    				Table t = getTableFromCache(tableName.getName());
    				if(t != null) {
    					Column column = t.getColumnByName(paramName);
    					if(column != null) {
    						return column;
    					}
    				}
    			}
			}catch(NotFoundTableException e) {
			    throw new IllegalArgumentException("get tableNamesByQuery occer error:"+sql.toString(),e);
			}
			return null;
		}
	}
    
}
