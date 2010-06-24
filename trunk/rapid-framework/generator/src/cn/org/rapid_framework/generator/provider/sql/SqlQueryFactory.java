package cn.org.rapid_framework.generator.provider.sql;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
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
        ResultSetMetaData metadata = executeQueryForMetaData(ps);
		SelectSqlMetaData result = convert2SelectSqlMetaData(metadata); 
        result.setOperation("findByPage"); //FIXME 
        if(result.getColumnsSize() > 1) {
        	System.out.println("QueryResultMetaData.isInSameTable():"+result.isInSameTable()+" getQueryResultClassName:"+result.getQueryResultClassName());
        }else {
        	System.out.println("QueryResultMetaData.isInSameTable():"+result.isInSameTable());
        }
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
                Column column = table.getColumnBySqlName(m.getColumnName());
                if(column == null) {
                    //可以再尝试解析sql得到 column以解决 password as pwd找不开column问题
                	//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
                    column = new Column(table,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                    GLogger.debug("not found column:"+m.getColumnName()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
                    //isInSameTable以此种判断为错误
                }else {
                	GLogger.debug("found column:"+m.getColumnName()+" on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
                }
                result.addColumn(column);
            }else {
                Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
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
				result.add(param);			
			}
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
//    	SelectSqlMetaData n2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=:username and password =:password");
//    	SelectSqlMetaData n3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
//    	SelectSqlMetaData n4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
//    	SelectSqlMetaData n5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
//    	SelectSqlMetaData n6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password and birth_date between :birthDateBegin and :birthDateEnd limit :offset,:limit");
//    	SelectSqlMetaData n7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    	SelectSqlMetaData n8 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = :userId group by username");
    }
    
    public static class SelectSqlMetaData {
//    	public SelectParameter parameters;
    	String operation = null;
    	String multiPolicy = "many"; // many or one
    	Set<Column> columns = new LinkedHashSet<Column>();
    	String queryResultClassName = null;
    	List<SelectParameter> params = new ArrayList();
    	
    	String sourceSql; // source sql
    	String jdbcSql; // jdbc sql
    	String ibatisSql; //ibatis sql
    	String hql; //hibernate sql
    	public boolean isInSameTable() {
    		if(columns.isEmpty()) return false;
    		if(columns.size() == 1 && columns.iterator().next().getTable() != null) return true;
    		String preTableName = columns.iterator().next().getSqlName();
    		for(Column c :columns) {
    			Table table = c.getTable();
				if(table == null) {
					return false;
				}
				if(preTableName.equals(table.getSqlName())) {
					continue;
				}
    		}
    		return true;
    	}
    	public String getQueryResultClassName() {
    		if(columns.size() == 1) 
    			throw new IllegalArgumentException("only single column by query,cannot execute method:getQueryResultClassName(), operation:"+operation);
    		if(queryResultClassName != null) return queryResultClassName;
    		if(isInSameTable()) {
    			return columns.iterator().next().getTable().getClassName();
    		}else {
    			if(operation == null) return null;
    			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Result";
    		}
		}
		public void setQueryResultClassName(String queryResultClassName) {
			this.queryResultClassName = queryResultClassName;
		}
		//TODO columnsSize大于二并且不是在同一张表中,将创建一个QueryResultClassName类,同一张表中也要考虑创建类
		public int getColumnsSize() {
    		return columns.size();
    	}
    	public void addColumn(Column c) {
    		columns.add(c);
    	}
		public String getOperation() {
			return operation;
		}
		public void setOperation(String operation) {
			this.operation = operation;
		}
		public String getMultiPolicy() {
			return multiPolicy;
		}
		public void setMultiPolicy(String multiPolicy) {
			this.multiPolicy = multiPolicy;
		}
		public Set<Column> getColumns() {
			return columns;
		}
		public void setColumns(Set<Column> columns) {
			this.columns = columns;
		}
		public List<SelectParameter> getParams() {
			return params;
		}
		public void setParams(List<SelectParameter> params) {
			this.params = params;
		}
		
		public String getSourceSql() {
			return sourceSql;
		}
		public void setSourceSql(String sourceSql) {
			this.sourceSql = sourceSql;
			setJdbcSql(sourceSql);
			setHql(sourceSql);
			setIbatisSql(sourceSql);
		}
		public String getJdbcSql() {
			return jdbcSql;
		}
		public void setJdbcSql(String jdbcSql) {
			this.jdbcSql = jdbcSql;
		}
		public String getIbatisSql() {
			return ibatisSql;
		}
		public void setIbatisSql(String ibatisSql) {
			this.ibatisSql = ibatisSql;
		}
		public String getHql() {
			return hql;
		}
		public void setHql(String hql) {
			this.hql = hql;
		}
		public Column getColumnBySqlName(String sqlName) {
			for(Column c : getColumns()) {
				if(c.getSqlName().equalsIgnoreCase(sqlName)) {
					return c;
				}
			}
			return null;
		}
		public Column getColumnByName(String name) {
		    Column c = getColumnBySqlName(name);
		    if(c == null) {
		    	c = getColumnBySqlName(StringHelper.toUnderscoreName(name));
		    }
		    return c;
		}		
    }
    
    public static class SelectParameter {
    	String parameterClassName;
    	int parameterMode;
    	int parameterType;
    	String parameterTypeName;
    	int precision;
    	int scale;
    	String paramName;
    	public SelectParameter() {}
    	public SelectParameter(ParameterMetaData m,int i) throws SQLException {
    		this.parameterClassName = m.getParameterClassName(i);
    		this.parameterMode = m.getParameterMode(i);
    		this.parameterType = m.getParameterType(i);
    		this.parameterTypeName = m.getParameterTypeName(i);
    		this.precision = m.getPrecision(i);
    		this.scale = m.getScale(i);
    	}
		public String getParameterClassName() {
			return parameterClassName;
		}
		public void setParameterClassName(String parameterClassName) {
			this.parameterClassName = parameterClassName;
		}
		public int getParameterMode() {
			return parameterMode;
		}
		public void setParameterMode(int parameterMode) {
			this.parameterMode = parameterMode;
		}
		public int getParameterType() {
			return parameterType;
		}
		public void setParameterType(int parameterType) {
			this.parameterType = parameterType;
		}
		public String getParameterTypeName() {
			return parameterTypeName;
		}
		public void setParameterTypeName(String parameterTypeName) {
			this.parameterTypeName = parameterTypeName;
		}
		public int getPrecision() {
			return precision;
		}
		public void setPrecision(int precision) {
			this.precision = precision;
		}
		public int getScale() {
			return scale;
		}
		public void setScale(int scale) {
			this.scale = scale;
		}
		public String getParamName() {
			return paramName;
		}
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
		public String toString() {
			return "paramName:"+paramName+" parameterClassName:"+parameterClassName;
		}
    }

    public static class ResultSetMetaDataHolder {
    	public ResultSetMetaDataHolder() {
    	}
    	public ResultSetMetaDataHolder(ResultSetMetaData m, int i) throws SQLException {
            String catalogName = m.getCatalogName(i);
            String columnClassName = m.getColumnClassName(i);
            int columnDisplaySize = m.getColumnDisplaySize(i);
            String columnLabel = m.getColumnLabel(i);
            String columnName = m.getColumnName(i);
            
            int columnType = m.getColumnType(i);
            String columnTypeName = m.getColumnTypeName(i);
            int precision = m.getPrecision(i);
            int scale = m.getScale(i);
            
            String schemaName = m.getSchemaName(i);
            String tableName = m.getTableName(i);
            
            this.catalogName = catalogName;
            this.columnClassName = columnClassName ;
            this.columnDisplaySize = columnDisplaySize;
            this.columnLabel = columnLabel;
            this.columnName = columnName;
            this.columnType=  columnType;
            this.columnTypeName = columnTypeName;
            this.precision  = precision;
            this.scale = scale;
            this.schemaName = schemaName;
            this.tableName = tableName;    		
    	}
        String catalogName ;
        String columnClassName ;
        int columnDisplaySize ;
        String columnLabel ;
        String columnName;
        
        int columnType ;
        String columnTypeName ;
        int precision  ;
        int scale ;
        
        String schemaName ;
        String tableName ;
        public String getCatalogName() {
            return catalogName;
        }
        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }
        public String getColumnClassName() {
            return columnClassName;
        }
        public void setColumnClassName(String columnClassName) {
            this.columnClassName = columnClassName;
        }
        public int getColumnDisplaySize() {
            return columnDisplaySize;
        }
        public void setColumnDisplaySize(int columnDisplaySize) {
            this.columnDisplaySize = columnDisplaySize;
        }
        public String getColumnLabel() {
            return columnLabel;
        }
        public void setColumnLabel(String columnLabel) {
            this.columnLabel = columnLabel;
        }
        public String getColumnName() {
            return columnName;
        }
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
        public int getColumnType() {
            return columnType;
        }
        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }
        public String getColumnTypeName() {
            return columnTypeName;
        }
        public void setColumnTypeName(String columnTypeName) {
            this.columnTypeName = columnTypeName;
        }
        public int getPrecision() {
            return precision;
        }
        public void setPrecision(int precision) {
            this.precision = precision;
        }
        public int getScale() {
            return scale;
        }
        public void setScale(int scale) {
            this.scale = scale;
        }
        public String getSchemaName() {
            return schemaName;
        }
        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }
        public String getTableName() {
            return tableName;
        }
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
        
    }
}
