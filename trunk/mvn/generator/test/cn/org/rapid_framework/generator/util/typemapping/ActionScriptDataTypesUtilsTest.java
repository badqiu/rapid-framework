


package cn.org.rapid_framework.generator.util.typemapping;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ActionScriptDataTypesUtilsTest{

    
    protected ActionScriptDataTypesUtils actionScriptDataTypesUtils = null;;
    
    @Before
    public void setUp() throws Exception {
        
        
    }
    
    @After
    public void tearDown() throws Throwable{
    }
    
    @Test
    public void test_getPreferredAsType() throws Throwable{
        String javaType = "java.lang.Long";
        
        String result = actionScriptDataTypesUtils.getPreferredAsType(javaType );
        assertEquals(result,"Number");
    }
    
}

