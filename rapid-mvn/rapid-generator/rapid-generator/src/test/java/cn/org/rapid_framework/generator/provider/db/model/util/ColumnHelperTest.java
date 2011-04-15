package cn.org.rapid_framework.generator.provider.db.model.util;

import java.util.Arrays;

import junit.framework.TestCase;

import org.hsqldb.Types;

import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.util.ColumnHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class ColumnHelperTest extends TestCase {
	
	public void test_replace() {
		String[] results = ColumnHelper.removeHibernateValidatorSpecialTags(" @Min(123)  @Max(345)   @Length(min=1,max=2)");
		System.out.println(Arrays.toString(results));
		assertEquals("Min",results[0]);
		assertEquals("Max",results[1]);
		assertEquals("Length",results[2]);
		
		results = ColumnHelper.removeHibernateValidatorSpecialTags(null);
		assertTrue(results.length == 0);
		
		results = ColumnHelper.removeHibernateValidatorSpecialTags("    ");
		assertTrue(results.length == 0);
	}
	
	public void test() {
		Column c = new Column(null,Types.VARCHAR,"VARCHAR","username",3,30,true,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"@Length(max=3)");
		
		c = new Column(null,Types.VARCHAR,"VARCHAR","username",0,30,true,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"");
		
		c = new Column(null,Types.VARCHAR,"VARCHAR","username",0,30,false,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"@NotBlank ");
		
		c = new Column(null,Types.VARCHAR,"VARCHAR","email",0,30,false,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"@NotBlank @Email");
		
		c = new Column(null,Types.TINYINT,"byte","email",0,30,false,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"@NotNull @Email  @Max(127)");
		
		c = new Column(null,Types.SMALLINT,"short","email",0,30,false,false,false,false,"default value","remarks");
		assertEquals(ColumnHelper.getHibernateValidatorExpression(c),"@NotNull @Email  @Max(32767)");
	}
	
	public void test_long_max_value() {
		try {
		long maxValue = Long.parseLong(StringHelper.repeat("9", 80));
		System.out.println(maxValue);
		}catch(NumberFormatException e) {
			
		}
	}
}
