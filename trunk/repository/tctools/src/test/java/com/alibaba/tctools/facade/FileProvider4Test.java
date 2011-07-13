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
 * TODO Comment of FileProvider
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-12
 */
public class FileProvider4Test {

    public static File tsvFile;
    public static File configFile;
    public static File javaFile;
    

    static {
        try {
            tsvFile = new File("D:\\Workplace\\JavaEE\\jee\\alibaba\\alibaba-qa-tc-tools\\src\\test\\resources\\LoginCase.tsv");
            configFile=new File("D:\\Workplace\\JavaEE\\jee\\alibaba\\alibaba-qa-tc-tools\\src\\test\\resources\\generator.xml");
            javaFile=new File("D:\\Workplace\\JavaEE\\jee\\alibaba\\alibaba-qa-tc-tools\\src\\test\\java\\com\\alibaba\\tctools\\gencode\\LoginCaseTest.java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
