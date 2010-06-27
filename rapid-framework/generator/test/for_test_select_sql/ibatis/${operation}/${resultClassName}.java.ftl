${gg.setIgnoreOutput(sql.columnsCount <= 1 || sql.columnsInSameTable)}

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ${sql.resultClassName} implements java.io.Serializable {

	<#list sql.columns as column>
	private ${column.simpleJavaType} ${column.columnNameLower};
	</#list>

<@generateJavaColumns/>

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}

<#macro generateJavaColumns>
	<#list sql.columns as column>
	public void set${column.columnName}(${column.simpleJavaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	public ${column.simpleJavaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#list>
</#macro>