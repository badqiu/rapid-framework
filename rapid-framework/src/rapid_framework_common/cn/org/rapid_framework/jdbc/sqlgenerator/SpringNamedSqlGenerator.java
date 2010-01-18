package cn.org.rapid_framework.jdbc.sqlgenerator;

import java.util.List;

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
		StringBuilder sb = new StringBuilder("INSERT ").append(getTableName()).append(" (");
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(c.getSqlName());
			if(i < getColumns().size() - 1)
				sb.append(",");
		}
		sb.append(" ) VALUES ( ");

		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(getColumnPlaceholder(c));
			if(i < getColumns().size() - 1)
				sb.append(",");
		}
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
		if(isMultiPrimaryKey()) {
			return getUpdateByMultiPkSql();
		}else if(isSinglePrimaryKey()) {
			return getUpdateBySinglePkSql();
		}
		throw new IllegalStateException("not found primary key on table:"+table.getTableName());
	}

	public String getUpdateByMultiPkSql() {
		StringBuilder sb = new StringBuilder("UPDATE ").append(getTableName()).append(" SET (");
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < getColumns().size() - 1)
				sb.append(",");
		}

		sb.append(" ) WHERE ");

		for(int i = 0; i < getPrimaryKeyColumns().size(); i++) {
			Column c = getPrimaryKeyColumns().get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < getPrimaryKeyColumns().size() - 1)
				sb.append(" AND ");
		}
		return sb.toString();
	}

	public String getUpdateBySinglePkSql() {
		checkIsSinglePrimaryKey();

		StringBuilder sb = new StringBuilder("UPDATE ").append(getTableName()).append(" SET (");
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(c.getSqlName()+" = "+getColumnPlaceholder(c));
			if(i < getColumns().size() - 1)
				sb.append(",");
		}

		sb.append(" ) WHERE ");
		sb.append(getSinglePrimaryKeyWhere());
		return sb.toString();
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
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < getColumns().size(); i++) {
			Column c = getColumns().get(i);
			sb.append(c.getSqlName()+" " + c.getPropertyName());
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
		return c.getSqlName()+" = :id";
	}

	private void checkIsSinglePrimaryKey() {
		if(getPrimaryKeyColumns().size() != 1) {
			throw new IllegalStateException("expected single primary key on table:"+getTableName()+",but was primary keys:"+getPrimaryKeyColumns());
		}
	}

}
