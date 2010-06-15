package cn.org.rapid_framework.generator.provider.db.model;



import java.util.List;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.model.util.ColumnHelper;
import cn.org.rapid_framework.generator.util.ActionScriptDataTypesUtils;
import cn.org.rapid_framework.generator.util.DatabaseDataTypesUtils;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.JdbcType;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.TestDataGenerator;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class Column {
	/**
	 * Reference to the containing table
	 */
	private Table _table;

	/**
	 * The java.sql.Types type
	 */
	private int _sqlType;

	/**
	 * The sql typename. provided by JDBC driver
	 */
	private String _sqlTypeName;

	/**
	 * The name of the column
	 */
	private String _sqlName;

	/**
	 * True if the column is a primary key
	 */
	private boolean _isPk;

	/**
	 * True if the column is a foreign key
	 */
	private boolean _isFk;

	/**
	 * @todo-javadoc Describe the column
	 */
	private int _size;

	/**
	 * @todo-javadoc Describe the column
	 */
	private int _decimalDigits;

	/**
	 * True if the column is nullable
	 */
	private boolean _isNullable;

	/**
	 * True if the column is indexed
	 */
	private boolean _isIndexed;

	/**
	 * True if the column is unique
	 */
	private boolean _isUnique;

	/**
	 * Null if the DB reports no default value
	 */
	private String _defaultValue;
	
	/**
	 * The comments of column
	 */
	private String _remarks;
			
	/**
	 * @param table
	 * @param sqlType
	 * @param sqlTypeName
	 * @param sqlName
	 * @param size
	 * @param decimalDigits
	 * @param isPk
	 * @param isNullable
	 * @param isIndexed
	 * @param isUnique
	 * @param defaultValue
	 * @param remarks
	 */
	public Column(Table table, int sqlType, String sqlTypeName,
			String sqlName, int size, int decimalDigits, boolean isPk,
			boolean isNullable, boolean isIndexed, boolean isUnique,
			String defaultValue,String remarks) {
		_table = table;
		_sqlType = sqlType;
		_sqlName = sqlName;
		_sqlTypeName = sqlTypeName;
		_size = size;
		_decimalDigits = decimalDigits;
		_isPk = isPk;
		_isNullable = isNullable;
		_isIndexed = isIndexed;
		_isUnique = isUnique;
		_defaultValue = defaultValue;
		_remarks = remarks;
		
		GLogger.trace(sqlName + " isPk -> " + _isPk);
		
		initOtherProperties();
	}

	public Column() {
	}
	
	/**
	 * Gets the SqlType attribute of the Column object
	 * 
	 * @return The SqlType value
	 */
	public int getSqlType() {
		return _sqlType;
	}

	/**
	 * Gets the Table attribute of the DbColumn object
	 * 
	 * @return The Table value
	 */
	public Table getTable() {
		return _table;
	}

	/**
	 * Gets the Size attribute of the DbColumn object
	 * 
	 * @return The Size value
	 */
	public int getSize() {
		return _size;
	}

	/**
	 * Gets the DecimalDigits attribute of the DbColumn object
	 * 
	 * @return The DecimalDigits value
	 */
	public int getDecimalDigits() {
		return _decimalDigits;
	}

	/**
	 * Gets the SqlTypeName attribute of the Column object
	 * 
	 * @return The SqlTypeName value
	 */
	public String getSqlTypeName() {
		return _sqlTypeName;
	}

	/**
	 * Gets the SqlName attribute of the Column object
	 * 
	 * @return The SqlName value
	 */
	public String getSqlName() {
		return _sqlName;
	}

	
	/**
	 * Gets the Pk attribute of the Column object
	 * 
	 * @return The Pk value
	 */
	public boolean isPk() {
		return _isPk;
	}

	/**
	 * Gets the Fk attribute of the Column object
	 * 
	 * @return The Fk value
	 */
	public boolean isFk() {
		return _isFk;
	}

	/**
	 * Gets the Nullable attribute of the Column object
	 * 
	 * @return The Nullable value
	 */
	public  boolean isNullable() {
		return _isNullable;
	}

	/**
	 * Gets the Indexed attribute of the DbColumn object
	 * 
	 * @return The Indexed value
	 */
	public  boolean isIndexed() {
		return _isIndexed;
	}

	/**
	 * Gets the Unique attribute of the DbColumn object
	 * 
	 * @return The Unique value
	 */
	public boolean isUnique() {
		return _isUnique;
	}

	/**
	 * Gets the DefaultValue attribute of the DbColumn object
	 * 
	 * @return The DefaultValue value
	 */
	public  String getDefaultValue() {
		return _defaultValue;
	}
	
	public  String getRemarks() {
		return _remarks;
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for return value
	 */
	public int hashCode() {
		if(getTable() != null) {
			return (getTable().getSqlName() + "#" + getSqlName()).hashCode();
		}else {
			return (getSqlName()).hashCode();
		}
	}

	/**
	 * Describe what the method does
	 * 
	 * @param o
	 *            Describe what the parameter does
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for return value
	 */
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o instanceof Column) {
			Column other = (Column)o;
			if(getSqlName().equals(other.getSqlName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for return value
	 */
	public String toString() {
		return getSqlName();
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for return value
	 */
	protected  String prefsPrefix() {
		return "tables/" + getTable().getSqlName() + "/columns/" + getSqlName();
	}

	/**
	 * Sets the Pk attribute of the DbColumn object
	 * 
	 * @param flag
	 *            The new Pk value
	 */
	void setFk(boolean flag) {
		_isFk = flag;
	}
	
	public String getUnderscoreName() {
		return getSqlName().toLowerCase();
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public String getColumnNameFirstLower() {
		return StringHelper.uncapitalize(getColumnName());
	}
	
	public String getColumnNameLowerCase() {
		return getColumnName().toLowerCase();
	}
	/**
	 * @deprecated use getColumnNameFirstLower() instead
	 */
	public String getColumnNameLower() {
		return getColumnNameFirstLower();
	}
	
	public String getJdbcSqlTypeName() {
		String result = JdbcType.getJdbcSqlTypeName(getSqlType());
		//if(result == null) throw new RuntimeException("jdbcSqlTypeName is null column:"+getSqlName()+" sqlType:"+getSqlType());
		return result;
	}
	
	public String getColumnAlias() {
		return columnAlias;
	}
	
	public String getConstantName() {
		return StringHelper.toUnderscoreName(getColumnName()).toUpperCase();
	}
	
	public boolean getIsNotIdOrVersionField() {
		return !isPk();
	}
	
	/**得到 rapid-validation的验证表达式  */
	public String getValidateString() {
		return isNullable() ? getNoRequiredValidateString() :  "required " + getNoRequiredValidateString();
	}
	
	/**得到 rapid-validation的验证表达式: required min-value-800  */
	public String getNoRequiredValidateString() {
		return ColumnHelper.getRapidValidation(this);
	}

	/** 得到JSR303 bean validation(Hibernate Validator)的验证表达式: @NotNull @Min(100) @Max(800) */
	public String[] getHibernateValidatorConstraintNames() {
		return ColumnHelper.removeHibernateValidatorSpecialTags(getHibernateValidatorExprssion());
	}
	
	/** 得到JSR303 bean validation(Hibernate Validator)的验证表达式: @NotNull @Min(100) @Max(800) */
	public String getHibernateValidatorExprssion() {
		return hibernateValidatorExprssion;
	}

	public void setHibernateValidatorExprssion(String v) {
		hibernateValidatorExprssion = v;
	}
	
	public boolean getIsStringColumn() {
		return DatabaseDataTypesUtils.isString(getSqlType(), getSize(), getDecimalDigits());
	}
	
	public boolean getIsDateTimeColumn() {
		return DatabaseDataTypesUtils.isDate(getSqlType(), getSize(), getDecimalDigits());
	}
	
	public boolean getIsNumberColumn() {
		return DatabaseDataTypesUtils.isFloatNumber(getSqlType(), getSize(), getDecimalDigits()) 
			|| DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(), getDecimalDigits());
	}
	
	/** 检查是否包含某些关键字,关键字以逗号分隔 */
	public boolean contains(String keywords) {
		if(keywords == null) throw new IllegalArgumentException("'keywords' must be not null");
		return StringHelper.contains(getSqlName(), keywords.split(","));
	}
	
	public boolean isHtmlHidden() {
		return isPk() && _table.isSingleId();
	}
	
	/**
	 * 得到对应的javaType,如java.lang.String,
	 * @return
	 */
	public String getJavaType() {
		return javaType;
	}
	
	/**
	 * 得到简短的java.lang.javaType,如java.lang.String将返回String,而非java.lang包的,将直接返回getJavaType()
	 * @return
	 */
	public String getSimpleJavaType() {
		return StringHelper.removePrefix(getJavaType(), "java.lang.");
	}
	
	public String getAsType() {
		return asType;
	}
	
	public String getTestData() {
		return new TestDataGenerator().getDBUnitTestData(getColumnName(),getJavaType(),getSize());
	}
	
	public boolean isUpdatable() {
		return updatable;
	}

	public boolean isInsertable() {
		return insertable;
	}
	
	public String getEnumClassName() {
		return enumClassName;
	}
	
	public void setEnumString(String str) {
		this.enumString = str;
	}
	public String getEnumString() {
		return enumString;
	}
	public List<EnumMetaDada> getEnumList() {
		return StringHelper.string2EnumMetaData(getEnumString());
	}
	public boolean isEnumColumn() {
		return getEnumList() != null && !getEnumList().isEmpty();
	}
	
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setAsType(String asType) {
		this.asType = asType;
	}

	public void setEnumClassName(String enumClassName) {
		this.enumClassName = enumClassName;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
	
	public void setNullable(boolean v) {
		this._isNullable = v;
	}
	
	public void setUnique(boolean unique) {
		_isUnique = unique;
	}

	public void setPk(boolean v) {
		this._isPk = v;
	}

	private void initOtherProperties() {
		String normalJdbcJavaType = DatabaseDataTypesUtils.getPreferredJavaType(getSqlType(), getSize(), getDecimalDigits());
		javaType = GeneratorProperties.getProperty("java_typemapping."+normalJdbcJavaType,normalJdbcJavaType).trim();
		columnName = StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(getSqlName()));
		enumClassName = getColumnName()+"Enum";		
		asType = ActionScriptDataTypesUtils.getPreferredAsType(getJavaType());	
		columnAlias = StringHelper.emptyIf(getRemarks(), getColumnNameFirstLower());
		setHibernateValidatorExprssion(ColumnHelper.getHibernateValidatorExpression(this));
	}
	
	private String enumString = "";
	private String javaType;
	private String columnAlias;
	private String columnName;
	private String asType;	
	private String enumClassName;
	private boolean updatable = true;	
	private boolean insertable = true;
	private String hibernateValidatorExprssion;
//	private String rapidValidation;
	/**
	 * public enum ${enumClassName} {
	 * 		${enumAlias}(${enumKey},${enumDesc});
	 * 		private String key;
	 * 		private String value;
	 * }
	 * @author badqiu
	 */
	public static class EnumMetaDada {
		private String enumAlias;
		private String enumKey;
		private String enumDesc;
		public EnumMetaDada(String enumAlias, String enumKey, String enumDesc) {
			super();
			this.enumAlias = enumAlias;
			this.enumKey = enumKey;
			this.enumDesc = enumDesc;
		}
		
		public String getEnumAlias() {
			return enumAlias;
		}
		public String getEnumKey() {
			return enumKey;
		}
		public String getEnumDesc() {
			return enumDesc;
		}
	}
}
