/**
 * Project: tctools
 * 
 * File Created at 2011-7-11
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
package com.test.qdox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.org.rapid_framework.generator.util.FileHelper;

import com.alibaba.tctools.custom.TsvModel;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.IndentBuffer;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.Type;

/**
 * TODO Comment of JavaClassOpTest
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-11
 */
public class JavaClassOpTest {

    private JavaDocBuilder builder;
    private String         projectPath = "D:/Workplace/JavaEE/jee/alibaba/alibaba-qa-tc-tools/";
    private String         fileName    = projectPath + "src/test/java/com/alibaba/tctools/gencode/"
                                               + "LoginCaseTest.java";
    File                   file;

    @Before
    public void init() throws Exception {
        builder = new JavaDocBuilder();
        file = new File(fileName);
        builder.addSource(new FileReader(file));
    }

    @Test
    public void testPath() {

    }

    @Test
    public void testJavaModel() throws Exception {
        JavaSource src = builder.getSources()[0];
        JavaPackage pkg = src.getPackage();
        String[] imports = src.getImports();
        System.out.println(pkg.toString());
        System.out.println(Arrays.toString(imports));
        JavaClass cls = builder.getClassByName("com.alibaba.tctools.gencode.LoginCaseTest");
        System.out.println(cls.getName());
        JavaMethod[] method = cls.getMethods();
        JavaMethod m = method[0];
        //m.setReturns(new Type("java.lang.String"));
        System.out.println(src.getCodeBlock());
        FileWriter f=new FileWriter(file);
        f.write(src.getCodeBlock());
        f.close();
        //System.out.println(BeanUtils.describe(m));
        //System.out.println(BeanUtils.describe(m.getTags()[0]));

    }
    
    @Test
    public void buildTsvModelByJava() throws Exception {
        TsvModel model=new TsvModel();
        JavaSource src = builder.getSources()[0];
        //System.out.println(src.getCodeBlock());
        JavaPackage pkg = src.getPackage();
        System.out.print("[DEBUG] parsing 类:"+pkg.getName()+".");
        
        JavaClass cls=src.getClasses()[0];
        model.setClassName(cls.getName());
        System.out.println(model.getClassName());
        
        for(JavaMethod m:cls.getMethods()){
            System.out.println(m.getName());
            for(DocletTag t:m.getTags()){
                System.out.println("@"+t.getName()+" "+t.getValue());
            }
        }
        
        
        
    }
}
