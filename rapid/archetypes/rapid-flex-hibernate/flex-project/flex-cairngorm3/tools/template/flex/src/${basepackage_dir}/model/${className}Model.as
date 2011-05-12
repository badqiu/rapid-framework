<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

	package ${basepackage}.model
	{
		import ${basepackage}.domain.${className};
		import common.base.page.Page;

		public class ${className}Model
		{
			[Bindable]
			public var page:Page;
			
			[Bindable]
			public var ${classNameFirstLower}:${className};

		}
	}