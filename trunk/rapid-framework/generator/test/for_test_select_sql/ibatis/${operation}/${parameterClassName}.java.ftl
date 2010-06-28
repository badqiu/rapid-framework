
${gg.setIgnoreOutput(sql.params?size <= 4)}

public class ${sql.parameterClassName} extends BaseQuery {
	<#list sql.params as param>
		private ${param.preferredParameterJavaType} ${param.paramName};
	</#list>
	
	<#list sql.params as param>
		public ${param.preferredParameterJavaType} get${param.paramName?cap_first}() {
			return ${param.paramName};
		}
		public void set${param.paramName?cap_first}(${param.preferredParameterJavaType} v) {
			this.${param.paramName} = v;
		}
	</#list>
}
