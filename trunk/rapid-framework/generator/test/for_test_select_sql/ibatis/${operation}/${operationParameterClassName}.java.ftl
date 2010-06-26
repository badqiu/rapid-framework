
${gg.setIgnoreOutput(sql.params?size <= 4)}

public class ${sql.operationParameterClassName} extends BaseQuery {
	<#list sql.params as param>
		private ${param.parameterClassName} ${param.paramName};
	</#list>
	
	<#list sql.params as param>
		public ${param.parameterClassName} get${param.paramName?cap_first}() {
			return ${param.paramName};
		}
		public void set${param.paramName?cap_first}(${param.parameterClassName} v) {
			this.${param.paramName} = v;
		}
	</#list>
}
