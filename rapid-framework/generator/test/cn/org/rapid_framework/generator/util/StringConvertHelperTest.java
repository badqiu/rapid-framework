package cn.org.rapid_framework.generator.util;

import java.util.Map;

import junit.framework.TestCase;


public class StringConvertHelperTest extends  TestCase{
	
	public void test() {
		Map map = StringConvertHelper.string2Map("abc=123,diy='123',sex='blog'");
		
		System.out.println(map.keySet()+" "+map.values());
	}
}
