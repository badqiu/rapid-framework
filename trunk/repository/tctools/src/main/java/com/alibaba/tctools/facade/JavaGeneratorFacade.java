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

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

import com.alibaba.tctools.custom.TsvModel;

/**
 * 根据tsv生成java
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-13
 */
public class JavaGeneratorFacade {
    
    private static GeneratorFacade g;
    
    public void generatorJavaByTsv(File tsvFile,File configFile) throws Exception{
        TsvModel model = TsvModelBuilder.getTsvModelByTsv(FileProvider4Test.tsvFile,
                FileProvider4Test.configFile);

        g = new GeneratorFacade();
        g.deleteOutRootDir();
        g.getGenerator().setTemplateRootDirs("classpath:template");//模板目录自定义
        Map map = new HashMap();
        map.put("className", model.getClassName());
        map.put("model", model);
        g.generateByMap(map);
        Runtime.getRuntime().exec(
                "cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
    }
}
