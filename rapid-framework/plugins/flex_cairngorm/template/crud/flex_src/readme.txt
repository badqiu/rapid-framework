<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

//copy follow code to GlobalControler.as

			import ${basepackage}.${underscoreName}.command.*;
			import ${basepackage}.${underscoreName}.event.Delete${className}Event;
			import ${basepackage}.${underscoreName}.event.Get${className}Event;
			import ${basepackage}.${underscoreName}.event.List${className}Event;
			import ${basepackage}.${underscoreName}.event.Save${className}Event;
	
			// ${className} events and commands
			addCommand(List${className}Event.EVENT_NAME,List${className}Command);
			addCommand(Save${className}Event.EVENT_NAME,Save${className}Command);
			addCommand(Delete${className}Event.EVENT_NAME,Delete${className}Command);
			addCommand(Get${className}Event.EVENT_NAME,Get${className}Command);

//copy follow code to Services.mxml

	<!--${classNameFirstLower}FlexService-->
	<mx:RemoteObject endpoint="../messagebroker/amf"
					 id="${classNameFirstLower}FlexService"
					 destination="${classNameFirstLower}FlexService"
					 showBusyCursor="true"/>