/**
 * Project: alibaba-qa-tc-tools
 * 
 * File Created at 2011-7-8
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

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.facade.TsvModelBuilder;
import com.alibaba.tctools.util.JavaReadUtil;
import com.alibaba.tctools.util.TsvWriterUtil;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

/**
 *部分功能集成测试
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-8
 */
public class InteragationTest {

    private static GeneratorFacade g;

    /**
     * 测试根据tsv生成java
     * 
     * @throws Exception
     */
    @Test
    public void generatorJavaByTsv() throws Exception {
        TsvModel model = TsvModelBuilder.getTsvModelByTsv(FileProvider4Test.tsvFile,
                FileProvider4Test.configFile);
        g = new GeneratorFacade();
        g.deleteOutRootDir();
        g.getGenerator().setTemplateRootDirs("classpath:template");
        Map map = new HashMap();
        map.put("className", model.getClassName());
        map.put("model", model);
        g.generateByMap(map);
        Runtime.getRuntime().exec(
                "cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
    }

    /**
     * 测试根据java反向更新tsv
     * @throws Exception
     */
    @Test
    public void updateTsvByJava() throws Exception {
        try {
            TsvWriterUtil.write(FileProvider4Test.tsvFile,
                    JavaReadUtil.parse(FileProvider4Test.javaFile, FileProvider4Test.tsvFile));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void updateJavaByTsv() throws Exception{
        fail();
    }
    
    
}
