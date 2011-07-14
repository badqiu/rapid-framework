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
package com.alibaba.tctools.custom;

import java.io.File;

import cn.org.rapid_framework.generator.GeneratorProperties;

/**
 * TODO Comment of FilePathHelper
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-13
 */
public class FilePathHelper {
    public static String projectPath = "C:\\Users\\lai.zhoul\\Desktop\\tctools\\showcase\\project.qatest";

    public static void setProjectPath(String path) {
        projectPath = path;
    }

    public static File getFileByPath(String path) {
        String realPath = projectPath + path;
        System.out.println(realPath);
        return new File(realPath);
    }

    public static File getDestJavaPath(String path) throws Exception {
        GeneratorProperties.load(FilePathHelper.projectPath+"\\case\\config.xml");
        String realPath = projectPath+"\\src\\test\\java\\"
                + getBasepackageDir(GeneratorProperties.getProperty("basepackage")) + path;
        System.out.println(realPath);
        return new File(realPath);
    }

    public static String getBasepackageDir(String basepackage) {
        return basepackage.replace('.', '\\');
    }
}
