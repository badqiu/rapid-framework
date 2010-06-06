package cn.org.rapid_framework.generator.provider.db;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.JdbcType;
import cn.org.rapid_framework.generator.util.StringHelper;

public class SqlQueryFactory {
    
    public SelectSqlMetaData getByQuery(String sql) throws Exception {
        System.out.println("\n*******************************");
        System.out.println(" sql:"+sql);
        System.out.println("*********************************");
        
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
        result.setParams(parseSqlParameters(ps, sql));
        return result;
    }

	private SelectSqlMetaData convert2SelectSqlMetaData(
			ResultSetMetaData metadata) throws SQLException, Exception {
		SelectSqlMetaData result = new SelectSqlMetaData();
        for(int i = 1; i <= metadata.getColumnCount(); i++) {
            ResultSetMetaDataHolder m = new ResultSetMetaDataHolder(metadata, i);
            System.out.println(BeanHelper.describe(m));
            if(StringHelper.isNotBlank(m.getTableName())) {
                Table table = DbTableFactory.getInstance().getTable(m.getTableName());
                Column column = table.getColumnBySqlName(m.getColumnName());
                if(column == null) {
                    //可以再尝试解析sql得到 column以解决 password as pwd找不开column问题
                	//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
                    column = new Column(table,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                    System.out.println("not found on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
                    //isInSameTable以此种判断为错误
                }else {
                	System.out.println("found on table:"+table.getSqlName()+" "+BeanHelper.describe(column));
                }
                result.addColumn(column);
            }else {
                Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                result.addColumn(column);
                System.out.println("not found on table by table emtpty:"+BeanHelper.describe(column));
            }
        }
		return result;
	}

	private ResultSetMetaData executeQueryForMetaData(PreparedStatement ps)
			throws SQLException {
		ResultSetMetaData metadata =  null;
        try {
			ResultSet rs = ps.executeQuery();
			metadata = rs.getMetaData(); //还没有养老result set
		} catch (Exception e) {
			metadata = ps.getMetaData();
		}
		return metadata;
	}

	private void setPreparedStatementParameters(String sql, PreparedStatement ps)
			throws SQLException {
		ps.setMaxRows(3);
        ps.setFetchSize(3);
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

	private List<SelectParameter> parseSqlParameters(PreparedStatement ps,String sql) throws Exception {
//		try {
//			ZqlParser parser = new ZqlParser(new ByteArrayInputStream((sql+";").getBytes()));
//			ZStatement st = parser.readStatement();
//			if(st instanceof ZQuery) {
//				ZQuery q = (ZQuery)st;
//				System.out.println("\n ZQuery.where:"+q.getWhere());
//			}
//			System.out.println("ZStatement:"+st); 
//		}catch(Exception e) {
//			throw new RuntimeException("sql:"+sql,e);
//		}
		List result = new ArrayList();
		int size = sql.split("\\?").length;
		for(int i = 0; i < size; i++) {
			SelectParameter param = new SelectParameter();
			param.setParameterClassName("String");
			param.setParameterType(JdbcType.VARCHAR.TYPE_CODE);
			param.setPrecision(10);
			param.setScale(10);
			param.setParameterTypeName("String");
			param.setParamName("param"+i);
			result.add(param);
		}
		return result;
//		ParameterMetaData m = ps.getParameterMetaData();
//		int count = m.getParameterCount();
//		for(int i = 0; i < count; i++) {
//			try {
//				SelectParameter qp = new SelectParameter(m,i);
//				System.out.println("parameters:"+BeanHelper.describe(qp));
//			}catch(Exception e) {
////				e.printStackTrace();
//				return;
//			}
//		}

	}
    
    public static void main(String[] args) throws Exception {
    	SelectSqlMetaData t1 = new SqlQueryFactory().getByQuery("select * from user_info");
    	SelectSqlMetaData t2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=? and password =?");
    	SelectSqlMetaData t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	SelectSqlMetaData t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	SelectSqlMetaData t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	SelectSqlMetaData t6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =? limit ?,?");
    	SelectSqlMetaData t7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    }
    
    public static class SelectSqlMetaData {
//    	public SelectParameter parameters;
    	String operation = null;
    	String multiPolicy = "many"; // many or one
    	Set<Column> columns = new LinkedHashSet<Column>();
    	String queryResultClassName = null;
    	List<SelectParameter> params = new ArrayList();
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
