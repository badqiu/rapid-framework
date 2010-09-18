

package cn.org.rapid_framework.generator.util.sqlparse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class SqlTypeCheckerTest{

    
    protected SqlTypeChecker sqlTypeChecker = new SqlTypeChecker();
    
    @Before
    public void setUp() throws Exception {
        
        
    }
    
    @After
    public void tearDown() throws Throwable{
    }
    
    @Test
    public void test_isSelectSql() throws Throwable{
        
        boolean result = sqlTypeChecker.isSelectSql("       select * from hibernate" );
        assertTrue(result);
        
        result = sqlTypeChecker.isSelectSql("a select * from hibernate" );
        assertFalse(result);
        
        result = sqlTypeChecker.isSelectSql("/** abc 123 */ select * from hibernate" );
        assertTrue(result);
        
        result = sqlTypeChecker.isSelectSql("/** abc 123 */ select * from hibernate /* abc */" );
        assertTrue(result);
        
        result = sqlTypeChecker.isSelectSql("select * from hibernate --hibernate" );
        assertTrue(result);
        
        result = sqlTypeChecker.isSelectSql("--select * from hibernate --hibernate" );
        assertFalse(result);
        
        result = sqlTypeChecker.isSelectSql("/*select * from hibernate */" );
        assertFalse(result);
    }
    
    @Test
    public void test_isUpdateSql() throws Throwable{
        
        boolean result = sqlTypeChecker.isUpdateSql("update user set username = ? ,abc = ? where id = ?" );
        assertTrue(result);
    }
    
    @Test
    public void test_isDeleteSql() throws Throwable{
        
        boolean result = sqlTypeChecker.isDeleteSql("delete from userinfo" );
        assertTrue(result);
        
        result = sqlTypeChecker.isDeleteSql("sdelete from userinfo" );
        assertFalse(result);
    }
    
    @Test
    public void test_isInsertSql() throws Throwable{
        String sourceSql = "insert into userinfo (user) values (?)";
        
        boolean result = sqlTypeChecker.isInsertSql(sourceSql );
        assertTrue(result);
        
        result = sqlTypeChecker.isInsertSql("sinsert into userinfo (user) values (?)" );
        assertFalse(result);
        
        System.out.println(Boolean.class);
    }
    
    
}

