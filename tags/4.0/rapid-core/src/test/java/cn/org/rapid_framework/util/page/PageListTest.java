


package cn.org.rapid_framework.util.page;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


public class PageListTest{
    protected PageList pageList = new PageList();
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void test_toPaginator() throws Throwable{
        
        
        
        Paginator result = pageList.toPaginator();
        
        assertNotNull(result);
    }
    
    
}
