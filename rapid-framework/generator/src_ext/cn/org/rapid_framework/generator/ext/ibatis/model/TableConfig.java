package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import cn.org.rapid_framework.generator.ext.ibatis.IbatisSqlMapConfigParser;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.XMLHelper.NodeData;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator.util.typemapping.JavaPrimitiveTypeMapping;
import cn.org.rapid_framework.generator.util.typemapping.JdbcType;

public class TableConfig {
    public String sqlname;
    public String sequence;
    public String dummypk;
    public String remarks;
    
    public String subpackage;
    public String _package;
    public String autoswitchdatasrc;
    public String doName;
    
    public List<MetaColumn> columns = new ArrayList();
    public List<MetaOperation> operations = new ArrayList<MetaOperation>();
    public List<MetaResultMap> resultMaps = new ArrayList<MetaResultMap>();
    
    //for support 
    //<sql id="columns"><![CDATA[ ]]></sql id="columns">
    //<include refid="columns"/> 
    public List<MetaSql> includeSqls = new ArrayList<MetaSql>(); 

    public static TableConfig parseFromXML(InputStream reader) throws SAXException, IOException {
        NodeData nodeData = new XMLHelper().parseXML(reader);
        TableConfig config = new TableConfig();
        BeanHelper.copyProperties(config, nodeData.attributes);
        
        for(NodeData child : nodeData.childs) {
            // table/operation
            if("operation".equals(child.nodeName)) {
                MetaOperation target = new MetaOperation();
                BeanHelper.copyProperties(target, child.attributes);
                for(NodeData opChild : child.childs) {
                    // table/operation/extraparams
                    if("extraparams".equals(opChild.nodeName)) {
                        // table/operation/extraparams/param
                        for(NodeData paramNode : opChild.childs) {
                            MetaParam mp = new MetaParam();
                            BeanHelper.copyProperties(mp, paramNode.attributes);
                            target.extraparams.add(mp);
                        }
                    }else {
                        BeanHelper.setProperty(target, opChild.nodeName, opChild.innerXML);
                    }
                }
                config.operations.add(target);
            }
            // table/column
            if("column".equals(child.nodeName)) {
                MetaColumn target = new MetaColumn();
                BeanHelper.copyProperties(target, child.attributes);
                config.columns.add(target);
            }
            // table/sql
            if("sql".equals(child.nodeName)) {
                MetaSql target = new MetaSql();
                BeanHelper.copyProperties(target, child.attributes);
                target.sql = child.innerXML;
                config.includeSqls.add(target);
            }
            // table/resultmap
            if("resultmap".equals(child.nodeName)) {
                MetaResultMap target = new MetaResultMap();
                BeanHelper.copyProperties(target, child.attributes);
                // table/resultmap/column
                for(NodeData c : child.childs) {
                    if("column".equals(c.nodeName)) {
                        MetaColumn column = new MetaColumn();
                        BeanHelper.copyProperties(column, c.attributes);
                        target.columns.add(column);
                    }
                }
                config.resultMaps.add(target);
            }
        }
        return config;
//        XStream x = newXStream();
//        return (TableConfig)x.fromXML(reader);
    }

    public List<MetaResultMap> getResultMaps() {
        return resultMaps;
    }
    public void setResultMaps(List<MetaResultMap> resultMaps) {
        this.resultMaps = resultMaps;
    }

    public String getTableClassName() {
        if(StringHelper.isBlank(sqlname)) return null;
        String removedPrefixSqlName = Table.removeTableSqlNamePrefix(sqlname);
        return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(removedPrefixSqlName));
    }
    public Table getTable() throws Exception {
        Table t = TableFactory.getInstance().getTable(getSqlname());
        if(columns != null) {
            for(MetaColumn c : columns) {
                Column tableColumn = t.getColumnByName(c.getName());
                if(tableColumn != null)
                    tableColumn.setJavaType(c.getJavatype()); //FIXME 只能自定义javaType
            }
        }
        return t;
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
    public List<MetaColumn> getColumns() {
        if(columns == null) {
            columns = new ArrayList();
        }
		return columns;
	}
	public void setColumns(List<MetaColumn> column) {
		this.columns = column;
	}
	
	public List<MetaSql> getIncludeSqls() {
		return includeSqls;
	}

	public void setIncludeSqls(List<MetaSql> includeSqls) {
		this.includeSqls = includeSqls;
	}

	public List<MetaOperation> getOperations() {
        return operations;
    }
    public void setOperations(List<MetaOperation> operations) {
        this.operations = operations;
    }
    
    public MetaOperation findOperation(String operationName) {
    	MetaOperation operation = null;
        for(MetaOperation item : getOperations()){
        	if(item.getName().equals(operationName)) {
        		return item;
        	}
        }
        return null;
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
    
    public static List<Sql> toSqls(TableConfig table) throws SQLException, Exception {
        return Convert2SqlsProecssor.toSqls(table);
    }
    
    static class Convert2SqlsProecssor {
        
        public static List<Sql> toSqls(TableConfig table)  {
            List<Sql> sqls = new ArrayList<Sql>();
            for(MetaOperation op :table.getOperations()) {
                sqls.add(processOperation(op,table));
            }
            return sqls;
        }
        
        public static Sql toSql(TableConfig table,String operationName)  {
        	MetaOperation operation = table.findOperation(operationName);
            if(operation == null) throw new IllegalArgumentException("not found operation with name:"+operationName);
            return processOperation(operation, table);
        }
        
        private static Sql processOperation(MetaOperation op,TableConfig table) {
        	try {
            SqlFactory sqlFactory = new SqlFactory(getCustomSqlParameters(table));
            String sqlString = IbatisSqlMapConfigParser.parse(op.getSql(),toMap(table.includeSqls));
            String unescapeSqlString = StringHelper.unescapeXml(sqlString);
            String namedSql = SqlParseHelper.convert2NamedParametersSql(unescapeSqlString,":","");
            
            Sql sql = sqlFactory.parseSql(namedSql);
            LinkedHashSet<SqlParameter> finalParameters = addExtraParams2SqlParams(op.getExtraparams(), sql);
            sql.setParams(finalParameters);
            sql.setColumns(processWithCustomColumns(getCustomColumns(table),sql.getColumns()));
            
            sql.setIbatisSql(sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#","#")));
            sql.setIbatisSql(processSqlForMoneyParam(sql.getIbatisSql(),sql.getParams()));
            sql.setIbatis3Sql(sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#{","}"))); // FIXME 修正ibatis3的问题
            
            sql.setOperation(op.getName());
            sql.setMultiplicity(op.getMultiplicity());
            sql.setParameterClass(op.getParameterClass());
            sql.setResultClass(op.getResultClass());
            sql.setRemarks(op.getRemarks());
            sql.setTableSqlName(table.getSqlname());
            sql.setPaging(op.isPaging());
            sql.setSqlmap(op.getSqlmap());
            sql.setParamType(op.getParamtype());      
            return sql;
        	}catch(Exception e) {
                throw new RuntimeException("parse sql error on table:"+table+" operation:"+op.getName()+" sql:"+op.getSql(),e);
            }
        }

		private static LinkedHashSet<Column> processWithCustomColumns(List<Column> customColumns,LinkedHashSet<Column> columns) {
			for(Column c : columns) {
				Column custom = findByCustomColumnBySqlName(customColumns,c.getSqlName());
				if(custom != null) {
					c.setJavaType(custom.getJavaType());
				}
			}
			return columns;
		}
		
		private static Column findByCustomColumnBySqlName(List<Column> customColumns,String sqlName) {
            for(Column custom : customColumns) {
                if(custom.getSqlName().equalsIgnoreCase(sqlName)) {
                    return custom;
                }
            }
            return null;
        }

		private static LinkedHashSet<SqlParameter> addExtraParams2SqlParams(List<MetaParam> extraParams, Sql sql) {
			LinkedHashSet<SqlParameter> filterdExtraParameters = new LinkedHashSet<SqlParameter>();
			for(MetaParam mparam : extraParams) {
			    if(sql.getParam(mparam.getName()) == null) {
			        SqlParameter extraparam = new SqlParameter();
			        extraparam.setParameterClass(mparam.getJavatype());
			        extraparam.setColumnAlias(mparam.getColumnAlias()); // FIXME extraparam alias 有可能为空
			        extraparam.setParamName(mparam.getName());
			        filterdExtraParameters.add(extraparam);
			    }
			}
			boolean append = false; //FIXME append = false config by system property: generator.extraParams.append = false;
			if(append) {
				LinkedHashSet result = new LinkedHashSet(sql.getParams());
				result.addAll(filterdExtraParameters);
				return result;
			}else {
				filterdExtraParameters.addAll(sql.getParams());
				return filterdExtraParameters;
			}
		}        

		private static String processSqlForMoneyParam(String ibatisSql,LinkedHashSet<SqlParameter> params) {
			for(SqlParameter p : params) {
				if(p.getParameterClass().endsWith("Money")) {
					ibatisSql = StringHelper.replace(ibatisSql, "#"+p.getParamName()+"#", "#"+p.getParamName()+".cent"+"#");
				}
			}
			return ibatisSql;
		}
		
        private static Map<String, String> toMap(List<MetaSql> sql) {
            Map map = new HashMap();
            for(MetaSql s : sql) {
                map.put(s.id, s.sql);
            }
            return map;
        }

        private static List<Column> getCustomColumns(TableConfig table) throws Exception {
            List<Column> result = new ArrayList<Column>();
            Table t = TableFactory.getInstance().getTable(table.getSqlname());
            for(MetaColumn mc : table.getColumns()) {
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
    
        private static List<SqlParameter> getCustomSqlParameters(TableConfig table) throws Exception {
            List<SqlParameter> result = new ArrayList<SqlParameter>();
            Table t = TableFactory.getInstance().getTable(table.getSqlname());
            for(MetaOperation op : table.getOperations()) {
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
                        sqlParam.setParamName(param.getName());
                        sqlParam.setColumnAlias(param.getColumnAlias()); //FIXME 有可能为空
                        result.add(sqlParam);
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) throws IOException, SAXException {
//        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/trade_fund_bill.xml");
//        TableConfig metaTable = new TableConfig();
//        metaTable.columns.add(new MetaColumn());
//        metaTable.columns.add(new MetaColumn());
//        metaTable.columns.add(new MetaColumn());
//        TableConfig.parseFromXML(new FileInputStream(file));
//        System.out.println("\n"+TableConfig.parseFromXML(new FileInputStream(file)));
    }
    
    static class SimpleObject{
        int v1;
        int v2;
        public SimpleObject(int v1, int v2){
            this.v1 = v1;
            this.v2 = v2;
        }
    }
    
    public static class MetaSql {
        String id;
        String sql;
        public String toString() {
            return String.format("<sql id='%s'>%s</sql>",id,sql);
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getSql() {
            return sql;
        }
        public void setSql(String sql) {
            this.sql = sql;
        }
        
    }
    public static class MetaColumn {
        private String name;
        private String javatype;
        private String columnAlias;
        private String nullValue;
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
		public void setNullValue(String nullValue) {
			this.nullValue = nullValue;
		}
	    public String getNullValue() {
	        if(StringHelper.isBlank(nullValue)) {
	            return JavaPrimitiveTypeMapping.getDefaultValue(javatype);
	        }else {
	            return nullValue;
	        }
	    }
	    public boolean isHasNullValue() {
	        return JavaPrimitiveTypeMapping.getWrapperTypeOrNull(javatype) != null;
	    }
		public String toString() {
            return BeanHelper.describe(this).toString();
        }
    }

    public static class MetaParam extends MetaColumn {
    }

    public static class MetaResultMap {
        private String name;
        private List<MetaColumn> columns = new ArrayList();
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<MetaColumn> getColumns() {
            return columns;
        }
        public void setColumns(List<MetaColumn> columns) {
            this.columns = columns;
        }
    }
    
    public static class MetaOperation {
        public List<MetaParam> extraparams = new ArrayList<MetaParam>();
        public String name;
        public String resultClass;
        public String parameterClass;
        public String remarks;
        public String multiplicity = "many";
        public String paramtype = "object"; //object or primitive
        public String sql;
        public String sqlmap;
        public Sql parsedSql;
        public boolean paging = false;
        
        public String tableSqlName = null; //是否需要
        
        public String append;
        public String appendXmlAttributes = "";
        
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

        public String getMultiplicity() {
			return multiplicity;
		}

		public void setMultiplicity(String multiplicity) {
			this.multiplicity = multiplicity;
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
        
        public String getParamtype() {
			return paramtype;
		}

		public void setParamtype(String paramtype) {
			this.paramtype = paramtype;
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
