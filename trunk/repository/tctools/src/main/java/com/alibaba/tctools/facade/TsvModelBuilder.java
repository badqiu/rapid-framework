/**
 * Project: tctools
 * 
 * File Created at 2011-7-12
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
import java.util.List;
import java.util.Map;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.util.GeneratorConfigUtil;
import com.alibaba.tctools.util.JavaReadUtil;
import com.alibaba.tctools.util.ModelConvertUtil;
import com.alibaba.tctools.util.TsvReadUtil;

/**
 * TODO Comment of TsvModelBuilder
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-12
 */
public class TsvModelBuilder {
    
    /**
     * 根据tsv文件和配置项文件generator.xml返回model
     * @param tsvFile
     * @param configFile
     * @return
     * @throws Exception
     */
    public static TsvModel getTsvModelByTsv(File tsvFile, File configFile) throws Exception {
        TsvModel model = TsvReadUtil.parse(tsvFile);
        Map<String, String> configMap = GeneratorConfigUtil.parse(configFile);
        List<Map<String, String>> list = ModelConvertUtil.convert(model.getDataList(), configMap);
        model.setDataList(list);
        return model;
    }
    
    /**
     * 根据javaFile返回TsvModel
     * @param javaFile
     * @return
     * @throws Exception
     */
    public static TsvModel getTsvModelByJava(File javaFile,File tsvFile) throws Exception {
        
        return JavaReadUtil.parse(javaFile,tsvFile);
    }
    
    
}
