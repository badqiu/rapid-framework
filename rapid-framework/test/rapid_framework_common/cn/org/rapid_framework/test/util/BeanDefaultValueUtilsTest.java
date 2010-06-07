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

public class BeanDefaultValueUtilsTest extends TestCase {
	 
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Bean1 bean = new Bean1();
        BeanDefaultValueUtils.setBeanProperties(bean);
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getInteger1(),new Integer(1));
        assertEquals(bean.getLong1(),new Long(1));
        assertEquals(bean.getSqldate1(),new java.sql.Date(System.currentTimeMillis()));
        assertEquals(bean.getInt1(),1);
        assertEquals(bean.getSex(),SexEnumBean.F);
        assertEquals(bean.getChar1(),'1');
    }


    

    
}
