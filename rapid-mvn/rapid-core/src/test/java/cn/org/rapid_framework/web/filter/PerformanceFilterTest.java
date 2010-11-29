


package cn.org.rapid_framework.web.filter;

import javax.servlet.ServletContext;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockFilterConfig;

@RunWith(JMock.class)
public class PerformanceFilterTest{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected PerformanceFilter performanceFilter = new PerformanceFilter();

    
    //dependence class
    String beanName = "";
    ServletContext servletContext = null;
    
    @Before
    public void setUp() throws Exception {
        
        performanceFilter.setBeanName(beanName);
        performanceFilter.setServletContext(servletContext);
        
    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
    }
    
    MockFilterConfig filterConfig = new MockFilterConfig();
    @Test
    public void test_initFilterBean() throws Throwable{
        performanceFilter.init(filterConfig);
        performanceFilter.initFilterBean();
        Assert.assertEquals(performanceFilter.threshold,3000);
        Assert.assertEquals(performanceFilter.includeQueryString,false);
        
        filterConfig.addInitParameter("threshold", ""+5000);
        filterConfig.addInitParameter("includeQueryString", "true");
        performanceFilter.init(filterConfig);
        performanceFilter.initFilterBean();
        Assert.assertEquals(performanceFilter.threshold,5000);
        Assert.assertEquals(performanceFilter.includeQueryString,true);
        
    }
    
    @Test
    public void test_destroy() throws Throwable{
        performanceFilter.destroy();
        
    }
    
    
}
