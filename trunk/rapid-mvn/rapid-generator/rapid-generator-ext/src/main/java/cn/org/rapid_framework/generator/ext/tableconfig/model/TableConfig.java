package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.ext.tableconfig.IbatisSqlMapConfigParser;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.ColumnSet;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator.util.typemapping.JavaPrimitiveTypeMapping;
import cn.org.rapid_framework.generator.util.typemapping.JdbcType;

public class TableConfig {
    public String sqlName;
    public String sequence;
    public String dummyPk;
    public String remarks;
    
    public String subPackage;
    public String _package;
    public boolean autoSwitchDataSrc;
    public String className;
    
    public List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    public List<OperationConfig> operations = new ArrayList<OperationConfig>();
    public List<ResultMapConfig> resultMaps = new ArrayList<ResultMapConfig>();
    
    //for support 
    //<sql id="columns"><![CDATA[ ]]></sql id="columns">
    //<include refid="columns"/> 
    public List<SqlConfig> includeSqls = new ArrayList<SqlConfig>(); 

    public List<ResultMapConfig> getResultMaps() {
        return resultMaps;
    }
    public void setResultMaps(List<ResultMapConfig> resultMaps) {
        this.resultMaps = resultMaps;
    }

    public String getClassName() {
        if(StringHelper.isNotBlank(className)) return className;
        if(StringHelper.isBlank(sqlName)) return null;
        return StringHelper.toJavaClassName(sqlName);
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public Column getPkColumn() throws Exception {
    	if(StringHelper.isBlank(dummyPk)) {
    		return getTable().getPkColumn();
    	}else {
    		return getTable().getColumnByName(dummyPk);
    	}
    }
    
    public String getPackage() {
    	if(StringHelper.isBlank(_package)) {
    		_package = GeneratorProperties.getRequiredProperty("basepackage");
    	}
    	if(StringHelper.isBlank(subPackage)) {
    		return _package;
    	}else {
    		return _package+"."+subPackage;
    	}
    }
    public void setPackage(String pkg) {
    	this._package = pkg;
    }
    
    private Table table = null;
    public Table getTable() throws Exception {
    	if(table != null) return table;
    	
        table = TableFactory.getInstance().getTable(getSqlName());
        if(columns != null) {
            for(ColumnConfig c : columns) {
                Column tableColumn = table.getColumnByName(c.getName());
                if(tableColumn != null) {
                    tableColumn.setJavaType(c.getJavatype()); //FIXME 只能自定义javaType
                }
            }
        }
        if(StringHelper.isNotBlank(getDummyPk())) {
            Column c = table.getColumnBySqlName(getDummyPk());
            if(c != null) {
                c.setPk(true);
            }
        }
        table.setClassName(getClassName());
        if(StringHelper.isNotBlank(remarks)) {
            table.setTableAlias(remarks);
        }
        return table;
    }
    
    public String getSqlName() {
        return sqlName;
    }
    public void setSqlName(String sqlname) {
        this.sqlName = sqlname;
    }
    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public List<ColumnConfig> getColumns() {
        if(columns == null) {
            columns = new ArrayList();
        }
		return columns;
	}
	public void setColumns(List<ColumnConfig> column) {
		this.columns = column;
	}
	
	public List<SqlConfig> getIncludeSqls() {
		return includeSqls;
	}

	public void setIncludeSqls(List<SqlConfig> includeSqls) {
		this.includeSqls = includeSqls;
	}

	public List<OperationConfig> getOperations() {
        return operations;
    }
    public void setOperations(List<OperationConfig> operations) {
        this.operations = operations;
    }
    
    public OperationConfig findOperation(String operationName) {
    	OperationConfig operation = null;
        for(OperationConfig item : getOperations()){
        	if(item.getName().equals(operationName)) {
        		return item;
        	}
        }
        return null;
    }
    
    public String getDummyPk() {
        return dummyPk;
    }

    public void setDummyPk(String dummypk) {
        this.dummyPk = dummypk;
    }
    
    public String getRemarks() {
        return remarks;
    }


    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getSubPackage() {
        return subPackage;
    }

    public void setSubPackage(String subpackage) {
        this.subPackage = subpackage;
    }

	public boolean isAutoSwitchDataSrc() {
		return autoSwitchDataSrc;
	}
	
    public void setAutoSwitchDataSrc(boolean autoswitchdatasrc) {
        this.autoSwitchDataSrc = autoswitchdatasrc;
    }

    public void setDoName(String doname) {
        this.className = doname;
    }

    public String getBasepackage() {
    	return getPackage();
    }
    
    public String toString() {
        return "sqlname:"+sqlName;
    }
    
    private List<Sql> sqls;
    public List<Sql> getSqls() throws SQLException, Exception {
        if(sqls == null) {
            sqls = toSqls(this);
        }
        return sqls;
    }
    
    public static List<Sql> toSqls(TableConfig table) throws SQLException, Exception {
        return Convert2SqlsProecssor.toSqls(table);
    }
    
    /**
     * 用于将 OperationConfig.class 解析转换成 Sql.class对象 
     **/
    static class Convert2SqlsProecssor {
        
        public static List<Sql> toSqls(TableConfig table)  {
            List<Sql> sqls = new ArrayList<Sql>();
            for(OperationConfig op :table.getOperations()) {
                sqls.add(processOperation(op,table));
            }
            return sqls;
        }
        
        public static Sql toSql(TableConfig table,String operationName)  {
        	OperationConfig operation = table.findOperation(operationName);
            if(operation == null) throw new IllegalArgumentException("not found operation with name:"+operationName);
            return processOperation(operation, table);
        }
        
        private static Sql processOperation(OperationConfig op,TableConfig table) {
        	try {
                SqlFactory sqlFactory = new SqlFactory();
                String sqlString = IbatisSqlMapConfigParser.parse(op.getSql(),toMap(table.includeSqls));
                String unescapeSqlString = StringHelper.unescapeXml(sqlString);
                String namedSql = SqlParseHelper.convert2NamedParametersSql(unescapeSqlString,":","");
                
                Sql sql = sqlFactory.parseSql(namedSql);
                LinkedHashSet<SqlParameter> finalParameters = addExtraParams2SqlParams(op.getExtraparams(), sql);
                sql.setParams(finalParameters);
                sql.setColumns(processWithCustomColumns(getCustomColumns(table),sql.getColumns()));
                
                String ibatisSql = getIbatisSql(op, sql);
                sql.setIbatisSql(ibatisSql);
                sql.setMybatisSql(sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#{","}"))  + " "+op.getAppend()); // FIXME 修正ibatis3的问题
                
                sql.setOperation(op.getName());
                sql.setParameterClass(op.getParameterClass());
                sql.setResultClass(op.getResultClass());
                sql.setRemarks(op.getRemarks());
                sql.setPaging(op.isPaging());
                sql.setSqlmap(op.getSqlmap());
                sql.setResultMap(op.getResultMap());
                
                //FIXME 增加insert append="nowait"至 CDATA ]]>结尾的前面
                
                if(StringHelper.isNotBlank(op.getMultiplicity())) {
                    sql.setMultiplicity(op.getMultiplicity());
                }
                
                //FIXME 与dalgen的规则是否一致
                if(StringHelper.isNotBlank(op.getParamtype())) {
                    sql.setParamType(op.getParamtype());
                }else if(StringHelper.isBlank(op.getParamtype()) && (sql.isSelectSql() || sql.isDeleteSql())) {
                    sql.setParamType("primitive");
                }
                
                return sql;
        	}catch(Exception e) {
                throw new RuntimeException("parse sql error on table:"+table.getSqlName()+" operation:"+op.getName()+"() sql:"+op.getSql(),e);
            }
        }

        private static String getIbatisSql(OperationConfig op, Sql sql) {
            String ibatisNamedSql = sql.replaceWildcardWithColumnsSqlName(SqlParseHelper.convert2NamedParametersSql(op.getSql(),"#","#")) + " "+ StringHelper.defaultString(op.getAppend());
            String ibatisSql = processSqlForMoneyParam(ibatisNamedSql,sql.getParams());
            return ibatisSql;
        }

		private static LinkedHashSet<Column> processWithCustomColumns(List<Column> customColumns,LinkedHashSet<Column> columns) {
		    ColumnSet columnSet = new ColumnSet(customColumns);
			for(Column c : columns) {
                Column custom = columnSet.getBySqlName(c.getSqlName());
				if(custom != null) {
					c.setJavaType(custom.getJavaType());
				}
			}
			return columns;
		}

		private static LinkedHashSet<SqlParameter> addExtraParams2SqlParams(List<ParamConfig> extraParams, Sql sql) {
			LinkedHashSet<SqlParameter> filterdExtraParameters = new LinkedHashSet<SqlParameter>();
			for(ParamConfig extraParam : extraParams) {
			    SqlParameter param = sql.getParam(extraParam.getName());
                if(param == null) {
			        SqlParameter extraparam = new SqlParameter();
			        extraparam.setParameterClass(extraParam.getJavatype());
			        if(StringHelper.isNotBlank(extraParam.getColumnAlias())) {
			        	extraparam.setColumnAlias(extraParam.getColumnAlias()); // FIXME extraparam alias 现在的处理方式不好,应该不使用StringHelper.isNotBlank()判断
			        }
			        extraparam.setParamName(extraParam.getName());
			        filterdExtraParameters.add(extraparam);
			    }else {
			        param.setParameterClass(extraParam.getJavatype());
			        if(StringHelper.isNotBlank(extraParam.getColumnAlias())) {
			        	param.setColumnAlias(extraParam.getColumnAlias()); // FIXME extraparam alias 现在的处理方式不好,应该不使用StringHelper.isNotBlank()判断
			        }
			    }
			}
			if(GeneratorProperties.getBoolean("generator.extraParams.append", true)) {
				LinkedHashSet result = new LinkedHashSet(sql.getParams());
				result.addAll(filterdExtraParameters);
				return result;
			}else {
				filterdExtraParameters.addAll(sql.getParams());
				return filterdExtraParameters;
			}
		}        
		
		/**
		 * Money类的特殊转换，only for alipay 
		 **/
		private static String processSqlForMoneyParam(String ibatisSql,LinkedHashSet<SqlParameter> params) {
			for(SqlParameter p : params) {
				if(p.getParameterClass().endsWith("Money")) {
					ibatisSql = StringHelper.replace(ibatisSql, "#"+p.getParamName()+"#", "#"+p.getParamName()+".cent"+"#");
				}
			}
			return ibatisSql;
		}
		
        private static Map<String, String> toMap(List<SqlConfig> sql) {
            Map map = new HashMap();
            for(SqlConfig s : sql) {
                map.put(s.id, s.sql);
            }
            return map;
        }

        private static List<Column> getCustomColumns(TableConfig table) throws Exception {
            List<Column> result = new ArrayList<Column>();
            Table t = table.getTable();
            for(ColumnConfig mc : table.getColumns()) {
                Column c = t.getColumnByName(mc.getName());
                if(c == null) {
                    c = new Column(null, JdbcType.UNDEFINED.TYPE_CODE, "UNDEFINED",
                        mc.getName(), -1, -1, false,false,false,false,
                        "",mc.getColumnAlias());
                }
                c.setJavaType(mc.getJavatype());
                c.setColumnAlias(mc.getColumnAlias()); // FIXME ColumnConfig.getColumnAlias()有可能为空
                result.add(c);
            }
            return result;
        }
    
    }
    
    public static class SqlConfig {
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
    public static class ColumnConfig {
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

    public static class ParamConfig extends ColumnConfig {
    }

    public static class ResultMapConfig {
        private String name;
        private List<ColumnConfig> columns = new ArrayList();
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<ColumnConfig> getColumns() {
            return columns;
        }
        public void setColumns(List<ColumnConfig> columns) {
            this.columns = columns;
        }
    }
    
    public static class OperationConfig {
        public List<ParamConfig> extraparams = new ArrayList<ParamConfig>();
        public String name;
        public String resultClass;
        public String resultMap;
        public String parameterClass;
        public String remarks;
        public String multiplicity;
        public String paramtype;
        public String sql;
        public String sqlmap;
        public Sql parsedSql;
        public boolean paging = false;
        
        public String append = ""; // append为无用配置,only for alipay的兼容性
        public String appendXmlAttributes = "";
        
        public List<ParamConfig> getExtraparams() {
            return extraparams;
        }

        public void setExtraparams(List<ParamConfig> extraparams) {
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
        
        public String getResultMap() {
            return resultMap;
        }

        public void setResultMap(String resultMap) {
            this.resultMap = resultMap;
        }

        public String getAppend() {
            return append;
        }

        public void setAppend(String append) {
            this.append = append;
        }

        public String toString() {
            return BeanHelper.describe(this).toString();
        }
    }
    

}
