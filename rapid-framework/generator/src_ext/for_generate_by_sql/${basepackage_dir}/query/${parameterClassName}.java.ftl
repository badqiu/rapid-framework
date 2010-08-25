
${gg.setIgnoreOutput(sql.params?size <= 4)}
package ${basepackage}.query;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ${sql.parameterClassName} extends PageQuery implements java.io.Serializable {
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
}
