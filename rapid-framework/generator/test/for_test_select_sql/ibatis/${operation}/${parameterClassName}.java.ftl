
${gg.setIgnoreOutput(sql.params?size <= 4)}

public class ${sql.parameterClassName} extends BaseQuery {
	<#list sql.params as param>
		private ${param.simpleJavaType} ${param.paramName};
	</#list>
	
	<#list sql.params as param>
		public ${param.simpleJavaType} get${param.paramName?cap_first}() {
			return ${param.paramName};
		}
		public void set${param.paramName?cap_first}(${param.simpleJavaType} v) {
			this.${param.paramName} = v;
		}
	</#list>
}
