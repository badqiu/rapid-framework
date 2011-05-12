package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

/**
 * @author badqiu
 */
public class Column {

	private String sqlName;
	private String propertyName;
	private boolean isPrimaryKey;
	private boolean updatable = true;
	private boolean insertable = true;
	private boolean unique = false;

	public Column(String sqlName, String propertyName) {
		this(sqlName,propertyName,false);
	}

	public Column(String sqlName, String propertyName,boolean isPrimaryKey) {
		this.sqlName = sqlName;
		this.propertyName = propertyName;
		this.isPrimaryKey = isPrimaryKey;
	}
	
	public Column(String sqlName, String propertyName, boolean isPrimaryKey,boolean updatable, boolean insertable, boolean unique) {
		this.sqlName = sqlName;
		this.propertyName = propertyName;
		this.isPrimaryKey = isPrimaryKey;
		this.updatable = updatable;
		this.insertable = insertable;
		this.unique = unique;
	}

	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String toString() {
		return String.format("sqlName:%s propertyName:%s isPrimaryKey:%s",sqlName,propertyName,isPrimaryKey);
	}


}
