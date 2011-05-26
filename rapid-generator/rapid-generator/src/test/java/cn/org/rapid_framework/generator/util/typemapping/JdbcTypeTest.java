


package cn.org.rapid_framework.generator.util.typemapping;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JdbcTypeTest{

    
    protected JdbcType jdbcType = null;;
    
    @Before
    public void setUp() throws Exception {
        
        
    }
    
    @After
    public void tearDown() throws Throwable{
    }
    
    @Test
    public void test_getJdbcSqlTypeName() throws Throwable{
        int code = 1;
        
        String result = jdbcType.getJdbcSqlTypeName(code );
        assertNotNull(result);
    }
    
    @Test
    public void test_values() throws Throwable{
        
        JdbcType[] result = jdbcType.values();
    }
    
    @Test
    public void test_valueOf() throws Throwable{
        String param1 = "BIGINT";
        
        JdbcType result = jdbcType.valueOf(param1 );
        assertNotNull(result);
    }
    
    
}

