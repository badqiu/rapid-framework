package cn.org.rapid_framework.jdbc.sqlgenerator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Column;
import cn.org.rapid_framework.jdbc.sqlgenerator.metadata.Table;

/**
 * spring的命名参数sql生成工具类
 * @see Table
 * @author badqiu
 *
 */
public class SpringNamedSqlGenerator implements SqlGenerator{
	Table table;

	public SpringNamedSqlGenerator(Table table) {
		super();
		this.table = table;
	}

	public List<Column> getColumns() {
		return table.getColumns();
	}

	public String getTableName() {
		return table.getTableName();
	}

	public List<Column> getPrimaryKeyColumns() {
		return table.getPrimaryKeyColumns();
	}

	public Table getTable() {
		return table;
	}

	public boolean isMultiPrimaryKey() {
		return getPrimaryKeyColumns().size() > 1;
	}

	public boolean isSinglePrimaryKey() {
		return getPrimaryKeyColumns().size() == 1;
	}

	public String getInsertSql() {
		StringBuilder sb = new StringBuilder("INSERT INTO ").append(getTableName()).append(" (");
		
		List<String> insertColumns = new ArrayList(getColumns().size());
		List<String> insertPlaceholderColumns = new ArrayList(getColumns().size());
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			if(c.isInsertable()) {
				insertColumns.add(c.getSqlName());
				insertPlaceholderColumns.add(getColumnPlaceholder(c));
			}
		}
		
		sb.append(StringUtils.join(insertColumns.iterator(), ","));
		sb.append(" ) VALUES ( ");

		sb.append(StringUtils.join(insertPlaceholderColumns.iterator(), ","));
		sb.append(" ) ");
		return sb.toString();
	}

	public String getDeleteByPkSql() {
		if(isMultiPrimaryKey()) {
			return getDeleteByMultiPkSql();
		}else if(isSinglePrimaryKey()) {
			return getDeleteBySinglePkSql();
		}
		throw new IllegalStateException("not found primary key on table:"+table.getTableName());
	}

	public String getSelectByPkSql() {
		if(isMultiPrimaryKey()) {
			return getSelectByMultiPkSql();
		}else if(isSinglePrimaryKey()) {
			return getSelectBySinglePkSql();
		}
		throw new IllegalStateException("not found primary key on table:"+table.getTableName());
	}

	public String getUpdateByPkSql() {
		if(getPrimaryKeyColumns().size() == 0) {
			throw new IllegalStateException("not found primary key on table:"+table.getTableName());
		}
		
		StringBuilder sb = new StringBuilder("UPDATE ").append(getTableName()).append(" SET ");
		
		sb.append(StringUtils.join(getUpdateColumns().iterator(), ","));
		sb.append(" WHERE ");

		for(int i = 0; i < getPrimaryKeyColumns().size(); i++) {
			Column c = getPrimaryKeyColumns().get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < getPrimaryKeyColumns().size() - 1)
				sb.append(" AND ");
		}
		return sb.toString();
	}

	private List getUpdateColumns() {
		List<Column> columns = getColumns();
		List updateColumns = new ArrayList(columns.size());
		for(int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			if(c.isUpdatable() && !c.isPrimaryKey()) {
				updateColumns.add(c.getSqlName() + " = "+getColumnPlaceholder(c));
			}
		}
		return updateColumns;
	}

	public String getDeleteByMultiPkSql() {
		StringBuilder sb = new StringBuilder("DELETE FROM ").append(getTableName());

		sb.append(" WHERE ");

		List<Column> primaryKeyColumns = getPrimaryKeyColumns();
		for(int i = 0; i < primaryKeyColumns.size(); i++) {
			Column c = primaryKeyColumns.get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < primaryKeyColumns.size() - 1)
				sb.append(" AND ");
		}
		return sb.toString();
	}

	public String getDeleteBySinglePkSql() {
		checkIsSinglePrimaryKey();
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();

		StringBuilder sb = new StringBuilder("DELETE FROM ").append(getTableName());

		sb.append(" WHERE ");

		sb.append(getSinglePrimaryKeyWhere());
		return sb.toString();
	}

	public String getSelectByMultiPkSql() {
		StringBuilder sb = new StringBuilder("SELECT "+getColumnsSql()+" FROM " + getTableName()+" WHERE ");
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();
		for(int i = 0; i < primaryKeyColumns.size(); i++) {
			Column c = primaryKeyColumns.get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < primaryKeyColumns.size() - 1)
				sb.append(" AND ");
		}
		return sb.toString();
	}

	public String getSelectBySinglePkSql() {
		checkIsSinglePrimaryKey();
		List<Column> primaryKeyColumns = getPrimaryKeyColumns();

		StringBuilder sb = new StringBuilder("SELECT "+getColumnsSql()+" FROM " + getTableName()+" WHERE ");
		sb.append(getSinglePrimaryKeyWhere());
		return sb.toString();
	}

	public String getColumnsSql() {
		return getColumnsSql(null);
	}

	public String getColumnsSql(String columnPrefix) {
		String realPrefix = StringUtils.isEmpty(columnPrefix) ? "" : columnPrefix+".";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(realPrefix+c.getSqlName()+" " + c.getPropertyName());
			if(i < getColumns().size() - 1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	protected String getColumnPlaceholder(Column c) {
		return ":"+c.getPropertyName();
	}

	protected String getSinglePrimaryKeyWhere() {
		Column c = getPrimaryKeyColumns().get(0);
		return c.getSqlName()+" = ?";
	}

	private void checkIsSinglePrimaryKey() {
		if(getPrimaryKeyColumns().size() != 1) {
			throw new IllegalStateException("expected single primary key on table:"+getTableName()+",but was primary keys:"+getPrimaryKeyColumns());
		}
	}


}
