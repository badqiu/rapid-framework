<#if !isUseParamObject(sql)>
${gg.setIgnoreOutput(true)}
</#if>

<#include '/java_copyright.include'/>

package ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case};

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

<#include '/java_import.include'/>

/**
<#include '/java_description.include'/>
 */
public class ${sql.parameterClassName} <#if sql.paging> extends PageQuery</#if> implements java.io.Serializable {
	private static final long serialVersionUID = -5216457518046898601L;
	
	<#list sql.params as param>
	/** ${param.columnAlias!} */
	private ${param.preferredParameterJavaType} ${param.paramName};
	</#list>
	
	public ${sql.parameterClassName}() {
	}
	
	public ${sql.parameterClassName}(<#list sql.params as param>${param.preferredParameterJavaType} ${param.paramName} <#if param_has_next>,</#if></#list>) {
		<#list sql.params as param>
		this.${param.paramName} = ${param.paramName};
		</#list>
	}
	
	<#list sql.params as param>
	public ${param.preferredParameterJavaType} get${param.paramName?cap_first}() {
		return ${param.paramName};
	}
	public void set${param.paramName?cap_first}(${param.preferredParameterJavaType} ${param.paramName}) {
		this.${param.paramName} = ${param.paramName};
	}
	</#list>
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
