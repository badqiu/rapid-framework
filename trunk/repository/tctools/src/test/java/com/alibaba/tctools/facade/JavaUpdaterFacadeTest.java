package com.alibaba.tctools.facade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.custom.UniqueUIDMap;
import com.alibaba.tctools.util.MyBeanUtils;

import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingDeque;

public class JavaUpdaterFacadeTest {

    @Test
    public void updateJavaByTsv() throws Exception {
   
       JavaUpdaterFacade.updateJavaByTsv(FileProvider4Test.tsvFile,
                FileProvider4Test.configFile,FileProvider4Test.javaFile);

    }

    @Test
    public void getAddedList() {
        List<Map<String, String>> tList=new ArrayList<Map<String,String>>();
        Map map1=new UniqueUIDMap<String, String>();
        map1.put("uid", "1");
        tList.add(map1);
        Map map2=new UniqueUIDMap<String, String>();
        map2.put("uid", "2");
        tList.add(map2);
        
        List<Map<String, String>> jList=new ArrayList<Map<String,String>>();
        Map map3= new UniqueUIDMap<String, String>();
        map3.put("uid", "2");
        
        jList.add(map3);
        Map map4= new UniqueUIDMap<String, String>();
        map4.put("uid", "3");
        jList.add(map4);
        
        List result=JavaUpdaterFacade.getAddedList(tList, jList);
        System.out.println(result.get(0));
        Assert.assertEquals(1, result.size());
         
    }
    @Test
    public void getDeletedList() {
        List<Map<String, String>> tList=new ArrayList<Map<String,String>>();
        Map map1=new UniqueUIDMap<String, String>();
        map1.put("uid", "1");
        tList.add(map1);
        Map map2=new UniqueUIDMap<String, String>();
        map2.put("uid", "2");
        tList.add(map2);
        
        List<Map<String, String>> jList=new ArrayList<Map<String,String>>();
        Map map3=new UniqueUIDMap<String, String>();
        map3.put("uid", "2");
        
        jList.add(map3);
        Map map4=new UniqueUIDMap<String, String>();
        map4.put("uid", "3");
        jList.add(map4);
        
        List result=JavaUpdaterFacade.getDeletedList(tList, jList);
        System.out.println(result.get(0));
        Assert.assertEquals(1, result.size());
        
    }
    
    @Test
    public void getUpdatedList() {
        List<Map<String, String>> tList=new ArrayList<Map<String,String>>();
        Map map1=new UniqueUIDMap<String, String>();
        map1.put("uid", "1");
        tList.add(map1);
        Map map2=new UniqueUIDMap<String, String>();
        map2.put("uid", "2");
        tList.add(map2);
        
        List<Map<String, String>> jList=new ArrayList<Map<String,String>>();
        Map map3=new UniqueUIDMap<String, String>();
        map3.put("uid", "2");
        
        jList.add(map3);
        Map map4=new UniqueUIDMap<String, String>();
        map4.put("uid", "3");
        jList.add(map4);
        
        List result=JavaUpdaterFacade.getUpdatedList(tList, jList);
        System.out.println(result.get(0));
        Assert.assertEquals(1, result.size());
    }
    
    
}
