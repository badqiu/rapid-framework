package cn.org.rapid_framework.test.context;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class TestMethodContextTest {
    @Rule public TestName testName = new TestName();
    
    @Test
    public void test_alibaba() {
        System.out.println(getClass());
        System.out.println("current test method:"+ testName.getMethodName());
        Assert.assertEquals("test_alibaba", testName.getMethodName());
    }

    @Test
    public void test_foo() {
        Assert.assertEquals("test_foo", testName.getMethodName());
    }
    
}
