<#if (sql.paramType = 'object' || sql.columnsCount <= 1 || sql.params?size <= 6 )>
${gg.setIgnoreOutput(true)}
</#if>
package ${basepackage}.operation;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.iwallet.biz.common.util.money.Money;
import java.util.*;

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
	
	
	<#list sql.sqlSegments as seg>
	<#if seg.generateParameterObject>
	private ${seg.className} ${seg.className?uncap_first} = new ${seg.className}();
	<@genGetterAndSetter seg.className seg.className/>
	
	<#list seg.params as param>
	public ${param.preferredParameterJavaType} get${param.paramName?cap_first}() {
		return ${seg.className?uncap_first}.get${param.paramName?cap_first}();
	}
	public void set${param.paramName?cap_first}(${param.preferredParameterJavaType} ${param.paramName}) {
		this.${seg.className?uncap_first}.set${param.paramName?cap_first}(${param.paramName});
	}
	</#list>	
	</#if>
	</#list>
	
	<#macro genGetterAndSetter propertyName,javaType>
	public ${javaType} get${propertyName?cap_first}() {
		return ${propertyName};
	}
	public void set${propertyName?cap_first}(${javaType} ${propertyName}) {
		this.${propertyName} = ${propertyName};
	}		
	</#macro>	
}
