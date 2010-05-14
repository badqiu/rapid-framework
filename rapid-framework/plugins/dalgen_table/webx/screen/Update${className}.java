<#include "/macro.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 

/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package com.ukulele.web.trade.module.screen.mcenter;

import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;
import com.alibaba.service.form.Form;
import com.alibaba.service.form.Group;
import com.alibaba.service.template.TemplateContext;
import com.alibaba.turbine.service.rundata.RunData;
import com.alibaba.webx.WebxException;

import com.alipay.mcenter.common.service.facade.McenterFacade;
import com.alipay.mcenter.common.service.facade.request.UpdateDataSyncBatchRequest;
import com.alipay.mcenter.common.service.facade.result.OperateResult;


public class Update${className} extends McenterScreenBase {
    private static final Logger logger = LoggerFactory.getLogger(Update${className}.class);

    private ${className}Facade       ${classNameFirstLower}Facade;

    /** 
     * @param rundata
     * @param context
     * @throws WebxException
     * @see com.alibaba.turbine.module.screen.TemplateScreen#execute(com.alibaba.turbine.service.rundata.RunData, com.alibaba.service.template.TemplateContext)
     */
    @Override
    protected void execute(RunData rundata, TemplateContext context) throws WebxException {
        
        
        if(!rundata.getParameters().containsKey("doAction")) {
        }
        
        processAction(rundata,context);
    }
    
    public void processAction(RunData rundata, TemplateContext context){
        Form form = getForm(rundata);
        if (!form.isValid()) {
            return;
        }
        OperateResult r = ${classNameFirstLower}Facade.Update${className}(newUpdate${className}Request(rundata,form));
        setContextForResult(context,r);
    }
    
    private void setContextForResult(TemplateContext context,OperateResult result) {
        Utils.processResult(context, r);
    }
    
	private Update${className}Request newUpdate${className}Request(RunData rundata,Form form) {
		Group group = form.getGroup("update${className}");

        <#list table.notPkColumns as column>
        <#if column.isDateTimeColumn>
        Date ${column.columnNameFirstLower} = group.getField("${column.columnNameFirstLower}").getDateValue(new SimpleDateFormat("yyyy-MM-dd"));
        <#else>
        ${column.simpleJavaType} ${column.columnNameFirstLower} = group.getField("${column.columnNameFirstLower}").get${column.simpleJavaType}Value();
        </#if>
        </#list>
		
		Update${className}Request r = new Update${className}Request();
		<#list table.notPkColumns as column>
        r.set${column.columnName}(${column.columnNameFirstLower});
        </#list>
		
		return r;
	}

    public void set${className}Facade(${className}Facade ${classNameFirstLower}Facade) {
        this.${classNameFirstLower}Facade = ${classNameFirstLower}Facade;
    }
    
}
