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
                ps.setObject(i+1, null);
            }
        }
        
        System.out.println("\n*******************************");
        System.out.println(" sql:"+sql);
        System.out.println("*******************************");
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData m = rs.getMetaData();
        for(int i = 1; i <= m.getColumnCount(); i++) {
            QueryColumnMetadata qcm = newColumnMetadata(m, i);
            System.out.println(BeanUtils.describe(qcm));
            if(StringHelper.isNotBlank(qcm.getTableName())) {
                Table table = DbTableFactory.getInstance().getTable(qcm.getTableName());
                Column column = table.getRequiredColumnBySqlName(qcm.getColumnName());
                System.out.println("found on table:"+BeanUtils.describe(column));
            }else {
                Column column = new Column(null,qcm.getColumnType(),qcm.getColumnTypeName(),qcm.getColumnName(),qcm.getColumnDisplaySize(),qcm.scale,false,false,false,false,null,null);
                System.out.println("not found on table:"+BeanUtils.describe(column));
            }
        } 
        return null;
    }

    private QueryColumnMetadata newColumnMetadata(ResultSetMetaData m, int i)
                                                                             throws SQLException {
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
        
        QueryColumnMetadata qcm = new QueryColumnMetadata();
        qcm.catalogName = catalogName;
        qcm.columnClassName = columnClassName ;
        qcm.columnDisplaySize = columnDisplaySize;
        qcm.columnLabel = columnLabel;
        qcm.columnName = columnName;
        qcm.columnType=  columnType;
        qcm.columnTypeName = columnTypeName;
        qcm.precision  = precision;
        qcm.scale = scale;
        qcm.schemaName = schemaName;
        qcm.tableName = tableName;
        return qcm;
    }
    public static class QueryColumnMetadata {
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
    
    public static void main(String[] args) throws Exception {
        Table t1 = new SqlQueryFactory().getByQuery("select * from user_info");
        Table t2 = new SqlQueryFactory().getByQuery("select username,password pwd from user_info where username=? and password =?");
        Table t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
        Table t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
        Table t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
    }
    
}
