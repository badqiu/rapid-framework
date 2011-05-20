package cn.org.rapid_framework.page;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
/**
 * @author badqiu
 */
public class PageTest extends TestCase {
	
	public void test() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map m = BeanUtils.describe(new Page(1,2,100));
		System.out.println(m);
		System.out.println(m.keySet());
	}
}
