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

import cn.org.rapid_framework.generator.util.FileHelper;

/**
 * 硬编码，便于测试
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-12
 */
public class FileProvider4Test {

    public static File tsvFile;
    public static File configFile;
    public static File javaFile;
    public static String templateDir;
    

    static {
        try {
            tsvFile = new File("C:\\Users\\lai.zhoul\\Desktop\\tctools\\showcase\\project.qatest\\case\\LoginCase.tsv");
            configFile=new File("C:\\Users\\lai.zhoul\\Desktop\\tctools\\showcase\\project.qatest\\case\\config.xml");
            javaFile=new File("C:\\Users\\lai.zhoul\\Desktop\\tctools\\showcase\\project.qatest\\src\\test\\java\\com\\alibaba\\tctools\\gencode\\LoginCaseTest.java");
            templateDir="C:\\Users\\lai.zhoul\\Desktop\\tctools\\showcase\\project.qatest\\case\\template";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
