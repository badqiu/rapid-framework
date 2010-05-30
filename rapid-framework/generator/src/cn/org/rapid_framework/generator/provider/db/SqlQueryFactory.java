package cn.org.rapid_framework.generator.provider.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.JdbcType;
import cn.org.rapid_framework.generator.util.StringHelper;

public class SqlQueryFactory {
    
    public Table getByQuery(String sql) throws Exception {
        Connection conn = DbTableFactory.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        if(sql.contains("?")) {
            int size = sql.split("\\?").length;
            for(int i = 0; i < size; i++) {
            	try {
                ps.setObject(i+1, null);
            	}catch(Exception e) {
            		System.err.println("error on set parametet index:"+i+" cause:"+e);
            	}
            }
        }
        
        System.out.println("\n*******************************");
        System.out.println(" sql:"+sql);
        System.out.println("*******************************");
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
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
                }
                System.out.println("found on table:"+BeanUtils.describe(column));
            }else {
                Column column = new Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnName(),m.getColumnDisplaySize(),m.scale,false,false,false,false,null,null);
                System.out.println("not found on table:"+BeanUtils.describe(column));
            }
        } 
        return null;
    }
    
    public static void main(String[] args) throws Exception {
        Table t1 = new SqlQueryFactory().getByQuery("select * from user_info");
        Table t2 = new SqlQueryFactory().getByQuery("select username,password pwd from user_info where username=? and password =?");
        Table t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
        Table t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
        Table t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
        Table t6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =? limit 1,2");
    }
    
    public static class QueryResultMetadata {
    	
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
