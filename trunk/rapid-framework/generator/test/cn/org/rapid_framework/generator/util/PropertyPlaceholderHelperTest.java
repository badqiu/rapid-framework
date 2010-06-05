package cn.org.rapid_framework.generator.util;

import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class PropertyPlaceholderHelperTest extends TestCase {
	Properties props = new Properties();
	public void test() {
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
				"${", "}", ":", false);
		
		System.out.println(helper.replacePlaceholders("abc=${user.dir}", System.getProperties()));
	}
	
}
