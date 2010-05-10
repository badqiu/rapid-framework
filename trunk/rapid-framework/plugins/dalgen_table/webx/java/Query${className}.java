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


public class Query${className} extends McenterScreenBase {
    private static final Logger logger = LoggerFactory.getLogger(Query${className}.class);

    private ${className}Facade       ${classNameFirstLower}Facade;

    /** 
     * @param rundata
     * @param context
     * @throws WebxException
     * @see com.alibaba.turbine.module.screen.TemplateScreen#execute(com.alibaba.turbine.service.rundata.RunData, com.alibaba.service.template.TemplateContext)
     */
    @Override
    protected void execute(RunData rundata, TemplateContext context) throws WebxException {
        Form form = getForm(rundata);
        if (!form.isValid()) {
            return;
        }
        
        if(rundata.getParameters().containsKey("doAction")) {
        	OperateResult r = ${classNameFirstLower}Facade.Query${className}(newQuery${className}Request(rundata,form));
        	Utils.processResult(context, r);
        }
    }


	private Query${className}Request newQuery${className}Request(RunData rundata,Form form) {
		Group group = form.getGroup("query${className}");

        <#list table.notPkColumns as column>
        <#if column.isDateTimeColumn>
        ${column.javaType} ${column.columnNameFirstLower}Begin = group.getField("${column.columnNameFirstLower}Begin").getValue();
        ${column.javaType} ${column.columnNameFirstLower}End = group.getField("${column.columnNameFirstLower}End").getValue();
        <#else>
        ${column.javaType} ${column.columnNameFirstLower} = group.getField("${column.columnNameFirstLower}").getValue();
        </#if>
        </#list>
		
		Query${className}Request r = new Query${className}Request();
		<#list table.notPkColumns as column>
		<#if column.isDateTimeColumn>
        r.set${column.columnName}Begin(${column.columnNameFirstLower}Begin);
        r.set${column.columnName}End(${column.columnNameFirstLower}End);
        <#else>
		r.set${column.columnName}(${column.columnNameFirstLower});
        </#if>
        </#list>
		
		return r;
	}

    public void set${className}Facade(${className}Facade ${classNameFirstLower}Facade) {
        this.${classNameFirstLower}Facade = ${classNameFirstLower}Facade;
    }
    
}
