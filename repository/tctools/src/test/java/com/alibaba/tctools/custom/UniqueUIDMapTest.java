package com.alibaba.tctools.custom;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class UniqueUIDMapTest {

    @Test
    public void testEquals() {
        Map<String, String> map1=new UniqueUIDMap<String, String>();
        map1.put("uid", "1");
        map1.put("k1", "v1");
        
        Map<String, String> map2=new UniqueUIDMap<String, String>();
        map2.put("uid", "2");
        map2.put("k1", "v1");
        System.out.println(map1.equals(map2));
        
        Assert.assertTrue(!map1.equals(map2));
        
        Map<String, String> map3=new UniqueUIDMap<String, String>();
        map3.put("uid", "1");
        map3.put("k2", "v2");
        System.out.println(map1.equals(map3));
        Assert.assertTrue(map1.equals(map3));
        
        Set s=new HashSet();
        s.add(map1);
        s.add(map3);
        Assert.assertEquals(1, s.size());
        System.out.println(s);
    }

}
