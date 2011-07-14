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
package com.alibaba.tctools.facade;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.util.GeneratorConfigUtil;
import com.alibaba.tctools.util.ModelConvertUtil;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;

/**
 * TODO Comment of JavaUpdaterFacade
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-13
 */
public class JavaUpdaterFacade {

    public static void updateJavaByTsv(File tsvFile, File configFile, File javaFile)
            throws Exception {
        TsvModel tmodel = TsvModelBuilder.getTsvModelByTsv(tsvFile, configFile);
        TsvModel jmodel = TsvModelBuilder.getTsvModelByJava(javaFile, tsvFile);
        //tmodel,jmodel都以英文变量为键
        jmodel = ModelConvertUtil.convert(jmodel, GeneratorConfigUtil.parse(configFile));
        List<Map<String, String>> tList = tmodel.getDataList();
        List<Map<String, String>> jList = jmodel.getDataList();
        FileReader reader = new FileReader(javaFile);
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSource(reader);
        JavaSource src = builder.getSources()[0];
        JavaClass cls = src.getClasses()[0];
        String newCode = getUpdatedContent(src.getCodeBlock(), cls, tList, jList);
        reader.close();
        FileWriter writer = new FileWriter(javaFile, false);//覆盖写入新的java content
        writer.write(newCode);
        writer.close();

    }

    public static String getUpdatedContent(String oldCode, JavaClass cls,
                                           List<Map<String, String>> tList,
                                           List<Map<String, String>> jList) {

        oldCode = processAddedContent(oldCode, cls, getAddedList(tList, jList));
        oldCode = processDeletedContent(oldCode, cls, getDeletedList(tList, jList));
        oldCode = processUpdatedContent(oldCode, cls, getUpdatedList(tList, jList));

        return oldCode;
    }

    public static String processAddedContent(String oldCode, JavaClass cls,
                                             List<Map<String, String>> list) {
        if (list.size() == 0)
            return oldCode;
        System.out.println("[DEBUG]processAddedContent()");
        String oldClsCode = cls.getCodeBlock();
        for (Map<String, String> map : list) {

            cls.addMethod(newJavaMethodByMap(map));
        }
        oldCode=cls.getSource().getCodeBlock();
        System.out.println("[DEBUG]新的source code为=" + oldCode);
        return oldCode;

    }

    //??处理删除情況，未完善
    public static String processDeletedContent(String oldCode, JavaClass cls,
                                               List<Map<String, String>> list) {
        /**
        if (list.size() == 0)
            return oldCode;
        System.out.println("[DEBUG]processDeletedContent()");
        JavaClass newcls = new JavaClass();
        cls.getMethods()[2].setParentClass(null);
        for (Map<String, String> map : list) {
           // oldCode = oldCode.replace(findJavaMethodByMap(cls, map).getCodeBlock(), "");//将已经删除的method代码替换为空 }
            
        }
        oldCode=cls.getSource().getCodeBlock();
        System.out.println("[DEBUG]新的source code为=" + oldCode);**/
        return oldCode;

    }

    //默认覆盖现有所有方法的注释
    public static String processUpdatedContent(String oldCode, JavaClass cls,
                                               List<Map<String, String>> list) {

        if (list.size() == 0)
            return oldCode;
        System.out.println("[DEBUG]processUpdatedContent()");

        for (JavaMethod m : cls.getMethods()) {
            for (Map<String, String> map : list) {
                if (m.getTagByName("uid").getValue().equals(map.get("uid"))) {
                    updateJavaMethodByMap(m, map);
                }
            }
        }
        oldCode = cls.getSource().getCodeBlock();
        System.out.println("[DEBUG]新的source code为=" + oldCode);
        oldCode = addAnnotations(oldCode);//临时解决办法
        return oldCode;

    }

    public static JavaMethod newJavaMethodByMap(Map<String, String> map) {
        JavaMethod m = new JavaMethod();
        System.out.println(map);
        System.out.println("[DEBUG]newJavaMethodByMap:方法名=" + map.get("methodName"));
        m.setName(map.remove("methodName"));//设置方法名
        m.setModifiers(new String[] { "public" });//设置public
        m.setSourceCode(" ");//设置代码块 
        m.setTags(getTagsByMap(map));
        System.out.println("[DEBUG]新增的method code=" + m.getCodeBlock());
        return m;
    }

    public static void updateJavaMethodByMap(JavaMethod m, Map<String, String> map) {
        m.setName(map.get("methodName"));
        m.setTags(getTagsByMap(map));

    }

    public static JavaMethod findJavaMethodByMap(JavaClass cls, Map<String, String> map) {

        for (JavaMethod m : cls.getMethods()) {
            if (map.get("uid").equals(m.getTagByName("uid").getValue())) {
                System.out.println("[DEBUG]deleted uid=" + map.get("uid"));
                System.out.println("[DEBUG]deleted method=" + m.getCodeBlock());
                return m;
            }
        }

        return null;
    }

    public static List<DocletTag> getTagsByMap(Map<String, String> map) {
        List tags = new ArrayList();
        DocletTag tag = null;//遍历设置tag
        for (final Entry<String, String> entry : map.entrySet()) {
            tag = new DocletTag() {

                public String getValue() {
                    // TODO Auto-generated method stub
                    return entry.getValue();
                }

                public String[] getParameters() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public Map getNamedParameterMap() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public String getNamedParameter(String key) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public String getName() {
                    // TODO Auto-generated method stub
                    return entry.getKey();
                }

                public int getLineNumber() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public AbstractBaseJavaEntity getContext() {
                    // TODO Auto-generated method stub
                    return null;
                }
            };
            tags.add(tag);

        }
        return tags;
    }

    /**
     * 由于qdox忽略注解@Test，自己遍历Method加入@Test,暂时办法
     * 
     * @param oldCode
     * @return
     */
    public static String addAnnotations(String oldCode) {
        return oldCode.replaceAll("public void", "@Test\n \t public void");
    }

    public static List<Map<String, String>> getAddedList(List<Map<String, String>> tList,
                                                         List<Map<String, String>> jList) {
        List copy = new ArrayList(tList);
        copy.removeAll(jList);
        return copy;
    }

    public static List<Map<String, String>> getDeletedList(List<Map<String, String>> tList,
                                                           List<Map<String, String>> jList) {
        List copy = new ArrayList(jList);
        copy.removeAll(tList);
        return copy;
    }

    public static List<Map<String, String>> getUpdatedList(List<Map<String, String>> tList,
                                                           List<Map<String, String>> jList) {
        List copy = new ArrayList(tList);
        copy.retainAll(jList);
        System.out.println(copy);
        return copy;
    }

}
