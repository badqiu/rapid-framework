${gg.setIgnoreOutput(sql.columnsCount <= 1 || sql.columnsInSameTable)}
package ${basepackage}.query;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ${sql.resultClassName} implements java.io.Serializable {

	<#list sql.columns as column>
	/** ${column.columnAlias} */
	private ${column.simpleJavaType} ${column.columnNameLower};
	</#list>

	<#list sql.columns as column>
	public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {
		this.${column.columnNameLower} = ${column.columnNameLower};
	}
	
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	</#list>

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
