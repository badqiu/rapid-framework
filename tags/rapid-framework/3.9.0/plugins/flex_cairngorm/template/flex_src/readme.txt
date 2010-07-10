<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

///////////////////////////
// ${className} readme
///////////////////////////
	//copy follow code to appcommon.flex.GlobalControler.as

			import ${basepackage}.${underscoreName}.${className}Register;
	
			// ${className} event and command mapping
			${className}Register.initialiseCommands(this)

	//copy follow code to appcommon.flex.Services.mxml

	<!--${classNameFirstLower}FlexService-->
	<mx:RemoteObject endpoint="../messagebroker/amf"
					 id="${classNameFirstLower}FlexService"
					 destination="${classNameFirstLower}FlexService"
					 showBusyCursor="true"/>



// generator-insert-location