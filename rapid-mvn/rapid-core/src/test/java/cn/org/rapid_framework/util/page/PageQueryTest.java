


package cn.org.rapid_framework.util.page;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class PageQueryTest{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected PageQuery pageQuery = new PageQuery();

    
    //dependence class
    
    @Before
    public void setUp() throws Exception {
        

    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
    }
    
    
}
