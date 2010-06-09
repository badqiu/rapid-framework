package cn.org.rapid_framework.test.context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = TestMethodContextExecutionListener.class)
public class TestMethodContextTest {

    @Test
    public void test_alibaba() {
        System.out.println(getClass());
        System.out.println("current test method:"+ TestMethodContext.getMethodName());
        Assert.assertEquals("test_alibaba", TestMethodContext.getMethodName());
    }

    @Test
    public void blog() {
        System.out.println(getClass());
        System.out.println("current test method:"+ TestMethodContext.getMethodName());
        Assert.assertEquals("blog", TestMethodContext.getMethodName());
    }
    
}
