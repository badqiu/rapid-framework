
${gg.setIgnoreOutput(sql.params?size <= 4)}

public class ${sql.parameterClassName} extends BaseQuery implements java.io.Serializable {
	<#list sql.params as param>
	private ${param.preferredParameterJavaType} ${param.paramName};
	</#list>
	
	public ${sql.parameterClassName} {
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
