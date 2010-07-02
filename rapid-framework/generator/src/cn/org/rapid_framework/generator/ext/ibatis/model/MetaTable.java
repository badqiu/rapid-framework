package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.generator.util.BeanHelper;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MetaTable {
    public String sqlname;
    public String sequence;
    public List<MetaColumn> columns = new ArrayList();
    public List<MetaOperation> operations = new ArrayList<MetaOperation>();
    
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
    public List<MetaColumn> getColumns() {
        return columns;
    }
    public void setColumns(List<MetaColumn> columns) {
        this.columns = columns;
    }
    public List<MetaOperation> getOperations() {
        return operations;
    }
    public void setOperations(List<MetaOperation> operations) {
        this.operations = operations;
    }

    public static MetaTable parseFromXML(InputStream reader) {
        XStream x = newXStream();
        return (MetaTable)x.fromXML(reader);
    }

    private static XStream newXStream() {
        XStream x = new XStream(new DomDriver());
        x.alias("table", MetaTable.class);
        x.alias("column", MetaColumn.class);
        x.alias("param", MetaColumn.class);
        x.alias("operation", MetaOperation.class);
        x.useAttributeFor(String.class);
        x.useAttributeFor(Integer.class);
        x.useAttributeFor(Long.class);
        x.useAttributeFor(Boolean.class);
        return x;
    }
    
    public String toString() {
        return BeanHelper.describe(this).toString();
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        String file = "D:/dev/workspaces/workspace/rapid/generator/src/cn/org/rapid_framework/generator/ext/ibatis/trade_fund_bill.xml";
        MetaTable metaTable = new MetaTable();
        metaTable.columns.add(new MetaColumn());
        metaTable.columns.add(new MetaColumn());
        metaTable.columns.add(new MetaColumn());
        newXStream().toXML(metaTable, System.out);
        System.out.println("\n"+MetaTable.parseFromXML(new FileInputStream(file)));
    }
    
    public static class MetaColumn {
        private String name;
        private String javaType;
        private String columnAlias;
        public String getName() {
            return name;
        }
        public void setName(String sqlname) {
            this.name = sqlname;
        }
        public String getJavaType() {
            return javaType;
        }
        public void setJavaType(String javatype) {
            this.javaType = javatype;
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
    
    public static class MetaOperation {
        public List<MetaColumn> extraparams = new ArrayList();
        public String name;
        public String resultClass;
        public String parameterClass;
        public String remarks;
        public String multiPolicy = "many";
        public String sql;
        public String sqlmap;
        
        public String tableSqlName = null; //是否需要
        
        public List<MetaColumn> getExtraparams() {
            return extraparams;
        }

        public void setExtraparams(List<MetaColumn> extraparams) {
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

        public String toString() {
            return BeanHelper.describe(this).toString();
        }
    }
    

}
