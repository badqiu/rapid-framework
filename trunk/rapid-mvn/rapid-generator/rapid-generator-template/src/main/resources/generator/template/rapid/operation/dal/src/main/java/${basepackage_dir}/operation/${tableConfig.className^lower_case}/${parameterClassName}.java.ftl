<#if !isUseParamObject(sql)>
${gg.setIgnoreOutput(true)}
</#if>

<#include '/java_copyright.include'/>

package ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case};

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.iwallet.biz.common.util.money.Money;
import com.alipay.common.dal.util.PageQuery;
import java.util.*;

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
}
