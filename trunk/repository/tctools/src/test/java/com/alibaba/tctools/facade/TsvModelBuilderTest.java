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

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.facade.TsvModelBuilder;
import com.alibaba.tctools.util.JavaReadUtil;

/**
 * TODO Comment of TsvModelBuilder
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-12
 */
public class TsvModelBuilderTest {
    
    
    @Test
    public void getTsvModelByTsv() throws Exception{
       TsvModel result= TsvModelBuilder.getTsvModelByTsv(FileProvider4Test.tsvFile, FileProvider4Test.configFile);
       System.out.println(result.getDataList());
       Assert.assertEquals(2, result.getDataList().size());
    }
    
    @Test
    public void getTsvModelByJava() throws Exception{
        try{
      JavaReadUtil.parse(FileProvider4Test.javaFile, FileProvider4Test.tsvFile);
        }catch (Exception e) {
            fail();
            e.printStackTrace();
        }
    }
   
}
