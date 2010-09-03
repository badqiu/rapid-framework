


package cn.org.rapid_framework.generator.util.typemapping;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(JMock.class)
public class DatabaseDataTypesUtilsTest{

    private Mockery  context = new JUnit4Mockery();
    
    protected DatabaseDataTypesUtils databaseDataTypesUtils = null;;
    
    @Before
    public void setUp() throws Exception {
        
        
    }
    
    @After
    public void tearDown() throws Throwable{
    }
    
    @Test
    public void test_isString() throws Throwable{
        String javaType = "java.lang.String";
        
        boolean result = databaseDataTypesUtils.isString(javaType );
        assertTrue(result);
    }
    
    @Test
    public void test_isDate() throws Throwable{
        String javaType = "java.util.Date";
        
        boolean result = databaseDataTypesUtils.isDate(javaType );
        assertTrue(result);
    }
    
    @Test
    public void test_isFloatNumber() throws Throwable{
        String javaType = "java.lang.Float";
        
        boolean result = databaseDataTypesUtils.isFloatNumber(javaType );
        assertTrue(result);
    }
    
    @Test
    public void test_isIntegerNumber() throws Throwable{
        String javaType = "java.lang.Long";
        
        boolean result = databaseDataTypesUtils.isIntegerNumber(javaType );
        assertTrue(result);
    }
    
    @Test
    public void test_getPreferredJavaType() throws Throwable{
        int sqlType = 1;
        int size = 1;
        int decimalDigits = 1;
        
        String result = databaseDataTypesUtils.getPreferredJavaType(sqlType ,size ,decimalDigits );
        assertNotNull(result);
    }
    
    
}

