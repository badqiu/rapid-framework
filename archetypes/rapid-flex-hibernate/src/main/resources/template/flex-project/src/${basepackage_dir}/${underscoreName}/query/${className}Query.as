<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.query
{
	import common.base.page.*;

	[Bindable]
	[RemoteClass(alias="${basepackage}.query.${className}Query")]
	public class ${className}Query extends PageQuery
	{
		public function ${className}Query()
		{
		}
	
		<#list table.columns as column>
		<#if column.isDateTimeColumn && !column.contains("begin,start,end")>
		public var ${column.columnNameLower}Begin:${column.asType};
		public var ${column.columnNameLower}End:${column.asType};
		<#else>
		public var ${column.columnNameLower}:${column.asType};
		</#if>
		</#list>

	}
}