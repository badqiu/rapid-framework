/**
 * Project: tctools
 * 
 * File Created at 2011-7-13
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.tctools.facade;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorContext;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

import com.alibaba.tctools.custom.FilePathHelper;
import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.util.MyBeanUtils;

/**
 * 根据tsv生成java
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-13
 */
public class JavaGeneratorFacade {

    private static GeneratorFacade g;

    public static void generatorJavaByTsv(File tsvFile, File configFile) throws Exception {
        TsvModel model = TsvModelBuilder.getTsvModelByTsv(tsvFile, configFile);
        GeneratorProperties.load(FilePathHelper.projectPath+"\\case\\config.xml");
        g = new GeneratorFacade();
        g.deleteOutRootDir();
        
        System.out.println("load:="+FilePathHelper.projectPath+"\\case\\config.xml");
      
     //   g.getGenerator().setTemplateRootDirs("classpath:template");//模板目录自定义
        g.getGenerator().setTemplateRootDirs(FilePathHelper.projectPath+"\\case\\template");
        Map map = new HashMap();
        map.put("className", model.getClassName());
        map.put("model", model);
        g.generateByMap(map);
        Runtime.getRuntime().exec(
                "cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
    }
}
