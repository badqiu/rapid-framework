package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.generator.ext.ibatis.IbatisSqlMapConfigParser;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator.util.typemapping.JdbcType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MetaTable {
    public String sqlname;
    public String sequence;
    public String dummypk;
    public List<MetaColumn> column = new ArrayList();
    public List<MetaOperation> operation = new ArrayList<MetaOperation>();

    public static MetaTable parseFromXML(InputStream reader) {
        XStream x = newXStream();
        return (MetaTable)x.fromXML(reader);
    }

    private static XStream newXStream() {
        XStream x = new XStream(new DomDriver());
        x.alias("table", MetaTable.class);
        x.alias("column", MetaColumn.class);
        x.alias("param", MetaParam.class);
        x.alias("operation", MetaOperation.class);
        x.addImplicitCollection(MetaTable.class,"column",MetaColumn.class);
        x.addImplicitCollection(MetaTable.class,"operation",MetaOperation.class);
        x.useAttributeFor(int.class);
        x.useAttributeFor(long.class);
        x.useAttributeFor(boolean.class);
        x.useAttributeFor(float.class);
        x.useAttributeFor(double.class);
        x.useAttributeFor(short.class);
        x.useAttributeFor(byte.class);
        x.useAttributeFor(char.class);
        
        x.useAttributeFor(String.class);
        x.useAttributeFor(Integer.class);
        x.useAttributeFor(Long.class);
        x.useAttributeFor(Boolean.class);
        x.useAttributeFor(Double.class);
        x.useAttributeFor(Float.class);
        x.useAttributeFor(Short.class);
        x.useAttributeFor(Byte.class);
        return x;
    }
    public String getTableClassName() {
        if(StringHelper.isBlank(sqlname)) return null;
        return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(sqlname));
    }
    public String getSqlname() {
        return sqlname;
    }
    public void setSqlname(String sqlname) {
        this.sqlname = sqlname;
    }
    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public List<MetaColumn> getColumn() {
		return column;
	}
	public void setColumn(List<MetaColumn> column) {
		this.column = column;
	}
	public List<MetaOperation> getOperation() {
        return operation;
    }
    public void setOperation(List<MetaOperation> operations) {
        this.operation = operations;
    }
    public String getDummypk() {
        return dummypk;
    }

    public void setDummypk(String dummypk) {
        this.dummypk = dummypk;
    }

    public String toString() {
//        return BeanHelper.describe(this).toString();
        return "sqlname:"+sqlname;
    }
    List<Sql> sqls;
    public List<Sql> getSqls() throws SQLException, Exception {
        if(sqls == null) {
            sqls = toSqls(this);
        }
        return sqls;
    }
    
    public static List<Sql> toSqls(MetaTable table) throws SQLException, Exception {
        return Convert2SqlsProecssor.toSqls(table);
    }
    
    private static class Convert2SqlsProecssor {
        
        public static List<Sql> toSqls(MetaTable table) throws SQLException, Exception {
            List<Sql> sqls = new ArrayList<Sql>();
            for(MetaOperation op :table.getOperation()) {
                SqlFactory sqlFactory = new SqlFactory(getCustomSqlParameters(table),getCustomColumns(table));
//                System.out.println("process operation:"+op.getName()+" sql:"+op.getSql());
                String sqlString = IbatisSqlMapConfigParser.parse(op.getSql());
                String namedSql = SqlParseHelper.convert2NamedParametersSql(sqlString,":","");
                Sql sql = sqlFactory.parseSql0(namedSql);
                if(StringHelper.isNotBlank(op.getSqlmap())) {
                    sql.setIbatisSql(op.getSqlmap());
                    sql.setIbatis3Sql(op.getSqlmap());
                }else {
                    sql.setIbatisSql(sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#","#")));
                    sql.setIbatis3Sql(sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#{","}"))); // FIXME 修正ibatis3的问题
                }
                sql.setOperation(op.getName());
                sql.setMultiPolicy(op.getMultiPolicy());
                sql.setParameterClass(op.getParameterClass());
                sql.setResultClass(op.getResultClass());
                sql.setRemarks(op.getComments());
                sql.setTableSqlName(table.getSqlname());
                sql.setPaging(op.isPaging());
                sqls.add(sql);
            }
            return sqls;
        }        
        
        private static List<Column> getCustomColumns(MetaTable table) throws Exception {
            List<Column> result = new ArrayList<Column>();
            Table t = TableFactory.getInstance().getTable(table.getSqlname());
            for(MetaColumn mc : table.getColumn()) {
                Column c = t.getColumnByName(mc.getName());
                if(c == null) {
                    c = new Column(null, JdbcType.UNDEFINED.TYPE_CODE, "UNDEFINED",
                        mc.getName(), -1, -1, false,false,false,false,
                        "",mc.getColumnAlias());
                }
                c.setJavaType(mc.getJavatype());
                c.setColumnAlias(mc.getColumnAlias());
                result.add(c);
            }
            return result;
        }
    
        private static List<SqlParameter> getCustomSqlParameters(MetaTable table) throws Exception {
            List<SqlParameter> result = new ArrayList<SqlParameter>();
            Table t = TableFactory.getInstance().getTable(table.getSqlname());
            for(MetaOperation op : table.getOperation()) {
                if(op.getExtraparams() != null) {
                    for(MetaParam param : op.getExtraparams()) {
                        Column c = t.getColumnByName(param.getName());
                        if(c == null) {
                            c = new Column(null, JdbcType.UNDEFINED.TYPE_CODE, "UNDEFINED",
                                param.getName(), -1, -1, false,false,false,false,
                                "",param.getColumnAlias());
                        }
                        SqlParameter sqlParam = new SqlParameter(c);
                        sqlParam.setJavaType(param.getJavatype());
                        sqlParam.setColumnAlias(param.getColumnAlias());
                        result.add(sqlParam);
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/trade_fund_bill.xml");
        MetaTable metaTable = new MetaTable();
        metaTable.column.add(new MetaColumn());
        metaTable.column.add(new MetaColumn());
        metaTable.column.add(new MetaColumn());
        newXStream().toXML(MetaTable.parseFromXML(new FileInputStream(file)), System.out);
        System.out.println("\n"+MetaTable.parseFromXML(new FileInputStream(file)));
    }
    
    public static class MetaColumn {
        private String name;
        private String javatype;
        private String columnAlias;
        public String getName() {
            return name;
        }
        public void setName(String sqlname) {
            this.name = sqlname;
        }
        public String getJavatype() {
            return javatype;
        }
        public void setJavatype(String javatype) {
            this.javatype = javatype;
        }
        public String getColumnAlias() {
            return columnAlias;
        }
        public void setColumnAlias(String columnAlias) {
            this.columnAlias = columnAlias;
        }
        public String toString() {
            return BeanHelper.describe(this).toString();
        }
    }

    public static class MetaParam extends MetaColumn {
    }
    
    public static class MetaOperation {
        public List<MetaParam> extraparams = new ArrayList<MetaParam>();
        public String name;
        public String resultClass;
        public String parameterClass;
        public String remarks;
        public String multiPolicy = "many";
        public String sql;
        public String sqlmap;
        public Sql parsedSql;
        public String comments;
        public boolean paging = false;
        
        public String tableSqlName = null; //是否需要
        
        public List<MetaParam> getExtraparams() {
            return extraparams;
        }

        public void setExtraparams(List<MetaParam> extraparams) {
            this.extraparams = extraparams;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResultClass() {
            return resultClass;
        }

        public void setResultClass(String resultClass) {
            this.resultClass = resultClass;
        }

        public String getParameterClass() {
            return parameterClass;
        }

        public void setParameterClass(String parameterClass) {
            this.parameterClass = parameterClass;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getMultiPolicy() {
            return multiPolicy;
        }

        public void setMultiPolicy(String multiPolicy) {
            this.multiPolicy = multiPolicy;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getSqlmap() {
            return sqlmap;
        }

        public void setSqlmap(String sqlmap) {
            this.sqlmap = sqlmap;
        }

        public String getTableSqlName() {
            return tableSqlName;
        }

        public void setTableSqlName(String tableSqlName) {
            this.tableSqlName = tableSqlName;
        }
        
        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }
        
        public boolean isPaging() {
            return paging;
        }

        public void setPaging(boolean paging) {
            this.paging = paging;
        }

        public String toString() {
            return BeanHelper.describe(this).toString();
        }
    }
    

}
