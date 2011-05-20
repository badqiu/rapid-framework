package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;


import org.junit.Test;

import static org.junit.Assert.*;

public class TableTest {

	@Test
	public void fromClass()  {
		Table t = MetadataCreateUtils.createTable(CommontBean.class);

		assertEquals("commont_bean",t.getTableName());
		assertEquals(3,t.getColumns().size());

		Column c0 = t.getColumns().get(0);
		assertColumn(c0,"age","age",false);
		Column c1 = t.getColumns().get(1);
		assertColumn(c1,"password","password",false);
		Column c2 = t.getColumns().get(2);
		assertColumn(c2,"user_name","userName",false);
		System.out.println(t);
	}
	
	@Test
	public void fromClassWithAnnotation()   {
		Table t = MetadataCreateUtils.createTable(AnnotationTestBean.class);

		assertEquals("ann_test_bean",t.getTableName());
		assertEquals(3,t.getColumns().size());

		Column c0 = t.getColumns().get(0);
		assertColumn(c0,"ann_age","age",false);
		Column c1 = t.getColumns().get(1);
		assertColumn(c1,"password","password",false);
		Column c2 = t.getColumns().get(2);
		assertColumn(c2,"ann_id","userName",true);
		System.out.println(t);
	}

	private void assertColumn(Column c, String sqlName, String propertyName,
			boolean isPrimaryKey) {
		assertEquals(c.getSqlName(),sqlName);
		assertEquals(c.getPropertyName(),propertyName);
		assertEquals(c.isPrimaryKey(),isPrimaryKey);
	}
}
