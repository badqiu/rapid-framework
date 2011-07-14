/**
 * Project: tctools
 * 
 * File Created at 2011-7-14
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
package com.alibaba.tctools.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.alibaba.tctools.custom.FilePathHelper;
import com.alibaba.tctools.facade.JavaGeneratorFacade;
import com.alibaba.tctools.facade.JavaUpdaterFacade;
import com.alibaba.tctools.facade.TsvUpdaterFacade;

/**
 * 同步器插件goal 
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-14
 * @goal update
 */
public class MainUpdaterMojo extends AbstarctBaseMojo {

    /**
     * 例如：-Dtsv=LoginCase
     * 
     * @parameter expression="${tsv}"
     */
    public String tsv;

    /**
     * 例如：-Djava=LoginCase
     * 
     * @parameter expression="${java}"
     */
    public String java;

    /*
     * (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();

        try {
            currentThread.setContextClassLoader(getClassLoader());

            System.out.println("current project to build: " + getProject().getName() + "\n"
                    + getProject().getFile().getParent());

            System.out.println("project baseDir=" + getProject().getBasedir().getAbsolutePath());
            FilePathHelper.setProjectPath(getProject().getBasedir().getAbsolutePath());
            try {
                File tsvFile = FilePathHelper.getFileByPath("\\case\\" + tsv + ".tsv");
                File configFile = FilePathHelper.getFileByPath("\\case\\config.xml");
                File javaFile = FilePathHelper.getFileByPath("\\case\\" + java + ".java");
                //输入java参数时
                if ("".equals(tsv) && !("".equals(java))) {
                    TsvUpdaterFacade.updateTsvByJava(javaFile, tsvFile);
                }

                //输入参数为tsv时
                if (!("".equals(tsv)) && "".equals(java)) {
                    JavaUpdaterFacade.updateJavaByTsv(tsvFile, configFile, javaFile);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }

    }

}
