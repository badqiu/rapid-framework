<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.domain{
	
	import ${basepackage}.domain.*;
	import common.base.*;
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="${basepackage}.domain.${className}")]
	public class ${className} extends BaseEntity
	{
		
		//alias
		<#list table.columns as column>
		public static const ALIAS_${column.constantName}:String = "${column.columnAlias}";
		</#list>

		//date formats
		<#list table.columns as column>
			<#if column.isDateTimeColumn>
		public static const FORMAT_${column.constantName} : String = DateFormats.DATE_FORMAT;
			</#if>
		</#list>
				
		//columns START
		<#list table.columns as column>
		public var ${column.columnNameLower}:${column.asType};
		</#list>		
		//columns END

		<#list table.columns as column>
			<#if column.isDateTimeColumn>
		public var  ${column.columnNameLower}String:String;		
			</#if>
		</#list>	
		
		<@generateAsOneToMany/>
		<@generateAsManyToOne/>	
		
<#macro generateAsOneToMany>
	<#list table.exportedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	public var ${fkPojoClassVar}s:ArrayCollection = new ArrayCollection();
	
	</#list>
</#macro>

<#macro generateAsManyToOne>
	<#list table.importedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	public var ${fkPojoClassVar}:${fkPojoClass} ;

	</#list>
</#macro>
		
		
	}
	
	
}