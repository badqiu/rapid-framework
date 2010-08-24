${gg.setIgnoreOutput(sql.columnsCount <= 1 || sql.columnsInSameTable)}
package ${basepackage}.query;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ${sql.resultClassName} implements java.io.Serializable {

	<#list sql.columns as column>
	/** ${column.columnAlias!} */
	<#if column.simpleJavaType?ends_with('Money')>
	private Money ${column.columnNameLower} = new Money(0,0);
	<#else>
	private ${column.simpleJavaType} ${column.columnNameLower};
	</#if>
	</#list>

	<#list sql.columns as column>
	public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {
		<#if column.simpleJavaType?ends_with('Money')>
		if(${column.columnNameLower} == null) {
			this.${column.columnNameLower} = new Money(0,0);
		}else {
			this.${column.columnNameLower} = ${column.columnNameLower};
		}		
		<#else>	
		this.${column.columnNameLower} = ${column.columnNameLower};
		</#if>
	}
	
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	
	</#list>

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
