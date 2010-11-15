package cn.org.rapid_framework.test.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.TestCase;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import cn.org.rapid_framework.test.util.testbean.Bean1;
import cn.org.rapid_framework.test.util.testbean.SexEnumBean;
import cn.org.rapid_framework.util.DateConvertUtils;

public class BeanDefaultValueUtilsTest extends TestCase {
	 
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        BeanAssert.assertPropertiesNull(bean,"char1","int1");
        
        BeanDefaultValueUtils.setBeanProperties(bean);
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getInteger1(),new Integer(1));
        assertEquals(bean.getLong1(),new Long(1));
        assertEquals(DateConvertUtils.format(bean.getSqldate1(),"yyyyMMddHHmmss"),DateConvertUtils.format(new java.sql.Date(System.currentTimeMillis()),"yyyyMMddHHmmss"));
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getSex(),SexEnumBean.F);
        assertEquals(bean.getChar1(),'1');
        assertNotNull(bean.getArrayList());
        assertNotNull(bean.getListLong());
        assertNotNull(bean.getMap());
        assertNotNull(bean.getHashMap());
        assertNotNull(bean.getTestBean());
        assertNotNull(bean.getSet());
        assertNotNull(bean.getHashSet());
        assertNotNull(bean.getCollection());
        assertNotNull(bean.getStack());
        
        BeanAssert.assertPropertiesNotNull(bean);
    }
    public void test_not_empty() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        BeanAssert.assertPropertiesEmpty(bean,"char1","int1");
        BeanDefaultValueUtils.setBeanProperties(bean);
        BeanAssert.assertPropertiesNotEmpty(bean,"arrayList","set","collection","hashMap","hashSet","map","listLong","stack");

        
    }

    

    
}
