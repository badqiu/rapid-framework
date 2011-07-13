package com.alibaba.tctools.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import cn.org.rapid_framework.generator.GeneratorProperties;



/**
 * 代码生成器插件的主要goal
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @goal gen
 * @date 2011-7-7
 */
public class MainGeneratorMojo extends AbstarctGeneratorMojo {

    /**
     * 例如：-Dtsv=testcase
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
            try {
//               JavaGeneratorFacade.generatorByTsv(getFileByClassLoader(tsv + ".tsv"),
//                        getFileByClassLoader("generator.xml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }

    }

    public static File getFileByClassLoader(String resourceName) throws IOException {
        String pathToUse = resourceName;
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
                .getResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        urls = Thread.currentThread().getContextClassLoader().getResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        urls = ClassLoader.getSystemResources(pathToUse);
        while (urls.hasMoreElements()) {
            return new File(urls.nextElement().getFile());
        }
        throw new FileNotFoundException("classpath:" + resourceName);
    }

}
