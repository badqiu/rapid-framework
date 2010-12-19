<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.model{
	
	<#include "/actionscript_imports.include">
	
	[Bindable]
	[RemoteClass(alias="${basepackage}.model.${className}")]
	public class ${className} extends BaseEntity
	{
		
		//alias
		<#list table.columns as column>
		public static const ALIAS_${column.constantName}:String = "${column.columnAlias}";
		</#list>

		//date formats
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
		public static const FORMAT_${column.constantName} : String = DATE_FORMAT;
			</#if>
		</#list>
				
		//columns START
		<#list table.columns as column>
		public var ${column.columnNameLower}:${column.asType};
		</#list>		
		//columns END

		<#list table.columns as column>
			<#if column.isDateTimeColumn>
		[Transient]
		public function get ${column.columnNameLower}String():String{
			return DateConvertUtils.format(this.${column.columnNameLower},FORMAT_${column.constantName});
		}
				
			</#if>
		</#list>		
		
		
	}
	
	
}