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
package com.alibaba.tctools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.tctools.custom.TsvColumnConstants;
import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.custom.UniqueUIDMap;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaSource;



/**
 * TODO Comment of JavaReadUtil
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-12
 */
public class JavaReadUtil {
    
    public static TsvModel parse(File javafile,File tsvfile) throws IOException {
        FileInputStream in=new FileInputStream(javafile);
        InputStreamReader inReader=new InputStreamReader(in, "UTF-8");//以UTF-8读.java
        
        return mergeHeader(parse(inReader),TsvReadUtil.parseHeader(tsvfile));
    }
    
    /**
     * 从java file中解析model
     * @param reader
     * @return
     * @throws IOException
     */
    public static TsvModel parse(Reader reader) throws IOException {
        TsvModel model=new TsvModel();
        JavaDocBuilder builder= new JavaDocBuilder();
        builder.addSource(reader);
        JavaSource src = builder.getSources()[0];
        JavaPackage pkg = src.getPackage();
        System.out.print("[DEBUG]parsing 类:"+pkg.getName()+".");      
        JavaClass cls=src.getClasses()[0];
        model.setClassName(cls.getName());
        System.out.println(model.getClassName());
        
        model.setDataList(convertMethods2DataList(cls.getMethods()));
        reader.close();
        return model;
    }
    
    /**
     * 转换JavaMethod 信息为map list
     * @param value
     * @return
     */
    public static List<Map<String, String>> convertMethods2DataList(JavaMethod[] value){
        List<Map<String, String>> result=new ArrayList<Map<String,String>>();
        Map<String, String> map=null;
        //这里map代表一个method，对应tsv里的一行
        for(JavaMethod m:value){
            map=new UniqueUIDMap<String, String>();//保证插入顺序
            System.out.println("[DEBUG]parsing 方法: "+m.getName());
            map.put(TsvColumnConstants.JAVA_METHOD_NAME, m.getName());
            for(DocletTag t:m.getTags()){
                
                System.out.println("[DEBUG]parsing tags@"+t.getName()+" "+t.getValue());
                map.put(t.getName(), t.getValue());
                
            }
            //处理完每个方法，对应了每一行，传给自定义Processor检查？JavaMethodProcessor待完善
            //过滤无UID的JavaMethod
            if(!map.containsKey(TsvColumnConstants.JAVA_UID_NAME)) continue;
           System.out.println(map);
           result.add(map);
        }
        return result;
    }
    
    public static TsvModel mergeHeader(TsvModel model,String[] header){
        model.setHeader(header);
        return model;
    }
}
