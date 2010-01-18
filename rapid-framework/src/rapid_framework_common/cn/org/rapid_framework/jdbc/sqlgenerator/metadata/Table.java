package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;

public class Table {

	private String tableName;
	private List<Column> columns;

	public Table(String tableName, Column... columns) {
		this(tableName,Arrays.asList(columns));
	}

	public Table(String tableName, List<Column> columns) {
		this.setTableName(tableName);
		this.setColumns(columns);
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public List<Column> getPrimaryKeyColumns() {
		List result = new ArrayList();
		for(Column c : getColumns()) {
			if(c.isPrimaryKey())
				result.add(c);
		}
		return result;
	}

	public String toString() {
		return "tableName:"+getTableName()+" columns:"+getColumns();
	}

	public static Table fromClass(Class clazz) throws IntrospectionException {

		BeanInfo info = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		List columns = new ArrayList();
		for(PropertyDescriptor pd : pds) {
			if("class".equals(pd.getName()))
				continue;
			if(pd.getReadMethod() == null || pd.getWriteMethod() == null){
				continue;
			}
			columns.add(new Column(toUnderscoreName(pd.getName()),pd.getName()));
		}

		Table t = new Table(toUnderscoreName(clazz.getSimpleName()),columns);
		return t;
	}

	private static String toUnderscoreName(String name) {
		if(name == null) return null;

		String filteredName = name;
		if(filteredName.indexOf("_") >= 0 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}
		if(filteredName.indexOf("_") == -1 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}

		StringBuffer result = new StringBuffer();
		if (filteredName != null && filteredName.length() > 0) {
			result.append(filteredName.substring(0, 1).toLowerCase());
			for (int i = 1; i < filteredName.length(); i++) {
				String preChart = filteredName.substring(i - 1, i);
				String c = filteredName.substring(i, i + 1);
				if(c.equals("_")) {
					result.append("_");
					continue;
				}
				if(preChart.equals("_")){
					result.append(c.toLowerCase());
					continue;
				}
				if(c.matches("\\d")) {
					result.append(c);
				}else if (c.equals(c.toUpperCase())) {
					result.append("_");
					result.append(c.toLowerCase());
				}
				else {
					result.append(c);
				}
			}
		}
		return result.toString();
	}

}
