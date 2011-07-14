package com.alibaba.tctools.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.alibaba.tctools.custom.FilePathHelper;
import com.alibaba.tctools.facade.JavaGeneratorFacade;



/**
 * 代码生成器插件的主要goal
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @goal gen
 * @date 2011-7-7
 */
public class MainGeneratorMojo extends AbstarctBaseMojo {

    /**
     * 例如：-Dtsv=LoginCase
     * 
     * @parameter expression="${tsv}"
     */
    public String tsv;
    

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
            
            System.out.println("project baseDir="+getProject().getBasedir().getAbsolutePath());
            FilePathHelper.setProjectPath(getProject().getBasedir().getAbsolutePath());
            try {
              JavaGeneratorFacade.generatorJavaByTsv(FilePathHelper.getFileByPath("\\case\\"+tsv+".tsv"),FilePathHelper.getFileByPath("\\case\\config.xml"));
              
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }

    }


   

}
