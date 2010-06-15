package cn.org.rapid_framework.generator.provider.db.model.util;

import java.util.Arrays;

import junit.framework.TestCase;

public class ColumnHelperTest extends TestCase {
	
	public void test_replace() {
		String[] results = ColumnHelper.removeHibernateValidatorSpecialTags(" @Min(123)  @Max(345)   @Length(min=1,max=2)");
		System.out.println(Arrays.toString(results));
		assertEquals("Min",results[0]);
		assertEquals("Max",results[1]);
		assertEquals("Length",results[2]);
	}
}
