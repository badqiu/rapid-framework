package cn.org.rapid_framework.generator.provider.db;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;

public class SqlQueryFactory {
    
    public QueryResultMetaData getByQuery(String sql) throws Exception {
        Connection conn = DbTableFactory.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        handleParameterMetaData(ps);
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
            		}
            		System.err.println("error on set parametet index:"+i+" cause:"+e);
            	}
            }
        }
        
        System.out.println("\n*******************************");
        System.out.println(" sql:"+sql);
        System.out.println("*******************************");
        ResultSetMetaData metadata =  null;
        try {
			ResultSet rs = ps.executeQuery();
			metadata = rs.getMetaData(); //还没有养老result set
		} catch (Exception e) {
			metadata = ps.getMetaData();
		}
       
        QueryResultMetaData result = new QueryResultMetaData();
        for(int i = 1; i <= metadata.getColumnCount(); i++) {
            ResultSetMetaDataHolder m = new ResultSetMetaDataHolder(metadata, i);
            System.out.println(BeanUtils.describe(m));
            if(StringHelper.isNotBlank(m.getTableName())) {
                Table table = DbTableFactory.getInstance().getTable(m.getTableName());
                Column column = table.getColumnBySqlName(m.getColumnName());
                if(column == null) {
                    //可以再尝试解析sql得到 column以解决 password as pwd找不开column问题
                	//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
                    column = new Column(table,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                    System.out.println("not found on table:"+table.getSqlName()+" "+BeanUtils.describe(column));
                    //isInSameTable以此种判断为错误
                }else {
                	System.out.println("found on table:"+table.getSqlName()+" "+BeanUtils.describe(column));
                }
                result.addColumn(column);
            }else {
                Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                result.addColumn(column);
                System.out.println("not found on table by table emtpty:"+BeanUtils.describe(column));
            }
        } 
        //
        result.setOperation("findByPage");
        if(result.getColumnsSize() > 1) {
        	System.out.println("QueryResultMetaData.isInSameTable():"+result.isInSameTable()+" getQueryResultClassName:"+result.getQueryResultClassName());
        }else {
        	System.out.println("QueryResultMetaData.isInSameTable():"+result.isInSameTable());
        }
        return result;
    }

	private void handleParameterMetaData(PreparedStatement ps)
			throws SQLException {
		ParameterMetaData m = ps.getParameterMetaData();
		int count = m.getParameterCount();
		for(int i = 0; i < count; i++) {
			try {
			QueryParameterMetaData qp = new QueryParameterMetaData(m,i);
			 System.out.println("parameters:"+BeanUtils.describe(qp));
			}catch(Exception e) {
//				e.printStackTrace();
				return;
			}
		}
	}
    
    public static void main(String[] args) throws Exception {
    	QueryResultMetaData t1 = new SqlQueryFactory().getByQuery("select * from user_info");
    	QueryResultMetaData t2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=? and password =?");
    	QueryResultMetaData t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	QueryResultMetaData t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	QueryResultMetaData t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    	QueryResultMetaData t6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =? limit ?,?");
    	QueryResultMetaData t7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    }
    
    public static class QueryParameterMetaData {
    	String parameterClassName;
    	int parameterMode;
    	int parameterType;
    	String parameterTypeName;
    	int precision;
    	int scale;
    	public QueryParameterMetaData(ParameterMetaData m,int i) throws SQLException {
    		this.parameterClassName = m.getParameterClassName(i);
    		this.parameterMode = m.getParameterMode(i);
    		this.parameterType = m.getParameterType(i);
    		this.parameterTypeName = m.getParameterTypeName(i);
    		this.precision = m.getPrecision(i);
    		this.scale = m.getScale(i);
    	}
    }
    
    public static class QueryResultMetaData {
    	String operation = null;
    	String multiPloicy = "many";
    	Set<Column> columns = new LinkedHashSet<Column>();
    	String queryResultClassName = null;
    	Map params = new LinkedHashMap();
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
    	public boolean isResult() {
    		return getQueryResultClassName().endsWith("Result");
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
		public String getMultiPloicy() {
			return multiPloicy;
		}
		public void setMultiPloicy(String multiPloicy) {
			this.multiPloicy = multiPloicy;
		}
		public Set<Column> getColumns() {
			return columns;
		}
		public void setColumns(Set<Column> columns) {
			this.columns = columns;
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
