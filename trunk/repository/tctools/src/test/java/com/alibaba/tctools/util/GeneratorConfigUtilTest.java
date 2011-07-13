
package com.alibaba.tctools.util;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.tctools.facade.FileProvider4Test;


/**
 * TODO Comment of GeneratorConfigUtilTest
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-8
 */
public class GeneratorConfigUtilTest {

	@Test
	public void testParse() throws Exception {
		Map<String, String> result =GeneratorConfigUtil.parse(FileProvider4Test.configFile);
		Assert.assertNotNull(result);
		System.out.println(result);
		
	}

}
