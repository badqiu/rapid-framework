package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Ref;
import java.util.Date;

import org.junit.Test;


public class MetadataCreateUtilsTest {
    @Test
    public void test() {
        assertTrue(MetadataCreateUtils.isNativeJavaType(int.class));
        assertTrue(MetadataCreateUtils.isNativeJavaType(Integer.class));
        assertTrue(MetadataCreateUtils.isNativeJavaType(Date.class));
        assertTrue(MetadataCreateUtils.isNativeJavaType(Ref.class));
        assertFalse(MetadataCreateUtils.isNativeJavaType(null));
        assertFalse(MetadataCreateUtils.isNativeJavaType(AAA.class));
        assertFalse(MetadataCreateUtils.isNativeJavaType(new Integer[]{}.getClass()));
        assertFalse(MetadataCreateUtils.isNativeJavaType(new int[]{}.getClass()));
        System.out.println(new Integer[]{}.getClass().getName());
    }
    
    @Test
    public void testCreate() {
    	Table table = MetadataCreateUtils.createTable(CommentUserInfoBean.class);
    	assertEquals(table.getTableName(),"comment_user_info_bean");
    	
    	vefiryColumn(table.getPrimaryKeyColumns().get(0),"user_id",false,false,true);
    }
    
    private void vefiryColumn(Column column, String sqlName, boolean inserable,boolean unique,boolean updatable) {
		assertEquals(column.getSqlName(),sqlName);
		assertEquals(column.isInsertable(),inserable);
		assertEquals(column.isUnique(),unique);
		assertEquals(column.isUpdatable(),updatable);
	}

	private static class AAA {}
}
